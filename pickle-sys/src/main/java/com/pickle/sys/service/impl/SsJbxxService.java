package com.pickle.sys.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.pickle.utils.bean.ImportErrorRecord;
import com.pickle.sys.bean.SsJbxx;
import com.pickle.sys.bean.entity.SsJbxxEntity;
import com.pickle.sys.mapper.SsJbxxMapper;
import com.pickle.sys.service.ISsJbxxService;
import com.pickle.sys.validator.DataValidator;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.bean.BeanUtils;
import com.pickle.utils.constant.ThreadConstant;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class SsJbxxService extends BaseService<SsJbxx> implements ISsJbxxService {
    // 数据分批大小，根据数据库和字段长度调整，通常 1000-5000 条/批
    private static final int BATCH_SIZE = 3000;

    @Autowired
    private SsJbxxMapper ssJbxxMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Override
    public List<SsJbxx> queryPageList(SsJbxx ssJbxx) {
        List<SsJbxx> list = ssJbxxMapper.queryPageList(ssJbxx);
        return list;
    }

    @Override
    public List<SsJbxx> exportByRange(int offset, int limit) {
        return ssJbxxMapper.exportByRange(offset, limit);
    }

    @Override
    public void importBigExcel(MultipartFile file) {
        long startTime = System.currentTimeMillis();

        ArrayBlockingQueue<List<SsJbxxEntity>> queue = new ArrayBlockingQueue<>(ThreadConstant.QUEUE_SIZE);
        // 使用阻塞队列存储错误记录
        ArrayBlockingQueue<ImportErrorRecord> errorQueue = new ArrayBlockingQueue<>(ThreadConstant.MAX_ERROR_RECORDS);

        AtomicLong successCount = new AtomicLong(0);
        AtomicInteger totalReadCount = new AtomicInteger(0);

        // 消费者任务
        CompletableFuture<Void> consumerFuture = CompletableFuture.runAsync(() -> {
            while (true) {
                try {
                    List<SsJbxxEntity> batch = queue.poll(30, TimeUnit.SECONDS);
                    if (batch == null) {
                        log.info("消费者超时退出，当前已处理条数：{}", successCount.get());
                        break;
                    }

                    // 对批次中的数据进行UUID设置和类型转换
                    List<SsJbxx> ssJbxxList = new ArrayList<>();
                    for (SsJbxxEntity entity : batch) {
                        SsJbxx ssJbxx = BeanUtils.copyPropertiesByCglib(entity, SsJbxx.class);
                        ssJbxx.setSsJbxxUuid(UUIDUtil.newUUID());
                        ssJbxxList.add(ssJbxx);
                    }

                    int inserted = batchInsert(ssJbxxList, errorQueue);
                    successCount.addAndGet(inserted);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("消费者线程被中断", e);
                    break;
                }
            }
        }, ThreadConstant.executor);

        List<String> errList = new ArrayList<>();
        try {
            // Excel读取监听器
            EasyExcel.read(file.getInputStream(), SsJbxxEntity.class, new AnalysisEventListener<SsJbxxEntity>() {
                private final List<SsJbxxEntity> batchList = new ArrayList<>(BATCH_SIZE);
                private int currentRowNum = 1; // Excel行号从1开始

                @Override
                public void invoke(SsJbxxEntity data, AnalysisContext context) {
                    currentRowNum++;

                    try {
                        // 数据校验和清洗
                        String error = DataValidator.cleanAndValidate(data, currentRowNum);
                        if (StringUtils.hasText(error)) {
                            errList.add(error);
                        }else {
                            batchList.add(data);
                        }
                        // 校验通过，添加到批次
                        totalReadCount.incrementAndGet();

                        // 批次满时提交
                        if (batchList.size() >= BATCH_SIZE) {
                            submitBatch();
                        }

                    } catch (BizException e) {
                        // 记录错误，但继续处理后续数据
                        log.warn("数据校验失败: {}", e.getMessage());
                        try {
                            // 尝试添加到错误队列，如果队列满了就忽略（只记录前MAX_ERROR_RECORDS条错误）
                            errorQueue.offer(new ImportErrorRecord(
                                    currentRowNum,
                                    data.toString(), // 可以自定义数据的字符串表示
                                    e.getMessage()
                            ));
                        } catch (Exception ex) {
                            log.error("记录错误信息失败", ex);
                        }
                    }
                }

                private void submitBatch() {
                    try {
                        if (!batchList.isEmpty()) {
                            queue.put(new ArrayList<>(batchList));
                            batchList.clear();
                        }
                    } catch (InterruptedException e) {
                        log.error("数据入队失败", e);
                        Thread.currentThread().interrupt();
                    }
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext context) {
                    // 提交最后一批数据
                    submitBatch();
                    log.info("文件解析完毕，共读取行数：{}", totalReadCount.get());
                }

                @Override
                public void onException(Exception exception, AnalysisContext context) throws Exception {
                    log.error("解析Excel行时发生异常: {}", exception.getMessage());
                    super.onException(exception, context);
                }
            }).sheet().doRead();

            // 等待消费者完成
            consumerFuture.get(5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BizException(e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        long cost = (endTime - startTime) / 1000;

        // 收集错误记录
        List<ImportErrorRecord> errors = new ArrayList<>();
        errorQueue.drainTo(errors);

        String format = String.format("成功：%d条，失败：%d条，耗时：%d秒，错误详情：%s",
                successCount.get(),
                totalReadCount.get() - successCount.get(),
                cost,
                errList);
        log.info("导入完成：{}", format);

    }

    /**
     * 执行批量插入，使用事务模板确保一批数据要么全成功，要么全失败。
     */
    private int batchInsert(List<SsJbxx> batch, ArrayBlockingQueue<ImportErrorRecord> errorQueue) {
        return transactionTemplate.execute(status -> {
            try {
                ssJbxxMapper.batchInsert(batch);
                return batch.size();
            } catch (Exception e) {
                log.error("批量插入失败，批次大小：{}，错误信息：{}", batch.size(), e.getMessage());

                // 记录批次中每一条数据的错误
                for (SsJbxx item : batch) {
                    try {
                        errorQueue.offer(new ImportErrorRecord(
                                -1, // 无法获取原始行号，使用-1标记
                                item.toString(),
                                "数据库插入失败: " + e.getMessage()
                        ));
                    } catch (Exception ex) {
                        log.error("记录错误信息失败", ex);
                    }
                }

                status.setRollbackOnly();
                return 0;
            }
        });
    }
}