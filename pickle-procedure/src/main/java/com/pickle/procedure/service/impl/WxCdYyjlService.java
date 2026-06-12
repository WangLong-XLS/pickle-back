package com.pickle.procedure.service.impl;

import com.pickle.procedure.bean.WxCdCcxx;
import com.pickle.procedure.bean.WxCdYyjl;
import com.pickle.procedure.mapper.WxCdCcxxMapper;
import com.pickle.procedure.mapper.WxCdYyjlMapper;
import com.pickle.procedure.service.IWxCdYyjlService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WxCdYyjlService extends BaseService<WxCdYyjl> implements IWxCdYyjlService {
    private final WxCdYyjlMapper wxCdYyjlMapper;
    private final WxCdCcxxMapper cdCcxxMapper;


    @Value("${wx.miniapp.appid:}")
    private String appid;
//
//    @Value("${wechat.pay.mchid}")
//    private String mchid;
//
//    @Value("${wechat.pay.apiV3Key}")
//    private String apiV3Key;
//
//    @Value("${wechat.pay.notifyUrl}")
//    private String notifyUrl;

    @Override
    public List<WxCdYyjl> queryPageList(WxCdYyjl wxCdYyjl) {
        List<WxCdYyjl> list = wxCdYyjlMapper.queryPageList(wxCdYyjl);
        return list;
    }

    @Override
    public void saveData(WxCdYyjl wxCdYyjl) {
        WxCdCcxx wxCdCcxx = new WxCdCcxx();
        wxCdCcxx.setCcyyUuid(wxCdYyjl.getCcyyUuid());
        wxCdCcxx.setDqRq(wxCdYyjl.getYyRq());
        List<WxCdCcxx> ccxxList = cdCcxxMapper.selectCcList(wxCdCcxx);
        WxCdCcxx cdCcxx = ccxxList.getFirst();
        if (cdCcxx.getCcYw().subtract(wxCdYyjl.getYyRs()).compareTo(BigDecimal.ZERO) < 0){
            throw new BizException("当前预约人数已超出！目前余位" +cdCcxx.getCcYw());
        }

        wxCdYyjl.setYyjlUuid(UUIDUtil.newUUID());
        wxCdYyjlMapper.insertSelective(wxCdYyjl);

        //去下单
//        this.jsapiPay(wxCdYyjl);
    }

    /**
     * JSAPI 下单
     */
   /* private Map<String, String> jsapiPay(WxCdYyjl wxCdYyjl) {
        WxUser wxUser = new WxUser();
        // 构建请求参数
        Map<String, Object> params = new HashMap<>();
        params.put("appid", appid);
        params.put("mchid", mchid);
        params.put("description", "畅打费");
        params.put("out_trade_no", "订单号");
        params.put("notify_url", notifyUrl);

        Map<String, Object> amountMap = new HashMap<>();
        amountMap.put("total", 10000);
        amountMap.put("currency", "CNY");
        params.put("amount", amountMap);

        Map<String, String> payerMap = new HashMap<>();
        payerMap.put("openid", wxUser.getOpenid());
        params.put("payer", payerMap);

        // 发送请求到微信支付（需要签名，建议使用官方SDK）
        String url = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";
        String jsonBody = JSON.toJSONString(params);

        // 生成签名并发送请求（具体签名实现请参考微信官方文档）
        String prepayId = sendHttpRequest(url, jsonBody);

        // 生成前端调起支付所需的参数
        return buildPayParams(prepayId, "outTradeNo");
    }*/

    /**
     * 构建前端调起支付参数
     */
/*    private Map<String, String> buildPayParams(String prepayId, String outTradeNo) {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = UUIDUtil.newUUID();
        String packageStr = "prepay_id=" + prepayId;

        // 签名串
        String signStr = appid + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageStr + "\n";

        // 使用商户私钥生成签名（RSA 签名）
        String paySign = generateRsaSign(signStr);

        Map<String, String> result = new HashMap<>();
        result.put("timeStamp", timeStamp);
        result.put("nonceStr", nonceStr);
        result.put("package", packageStr);
        result.put("signType", "RSA");
        result.put("paySign", paySign);

        return result;
    }*/

    @Override
    public void updateData(WxCdYyjl wxCdYyjl) {
        WxCdCcxx wxCdCcxx = new WxCdCcxx();
        wxCdCcxx.setCcyyUuid(wxCdYyjl.getCcyyUuid());
        wxCdCcxx.setDqRq(wxCdYyjl.getYyRq());
        List<WxCdCcxx> ccxxList = cdCcxxMapper.selectCcList(wxCdCcxx);
        WxCdCcxx cdCcxx = ccxxList.getFirst();
        if (cdCcxx.getCcYw().subtract(wxCdYyjl.getYyRs()).compareTo(BigDecimal.ZERO) < 0){
            throw new BizException("当前预约人数已超出！目前余位" +cdCcxx.getCcYw());
        }

        wxCdYyjlMapper.updateByPrimaryKeySelective(wxCdYyjl);
    }

    @Override
    public List<WxCdYyjl> selectYyjlList(WxCdYyjl wxCdYyjl) {
        return wxCdYyjlMapper.selectYyjlList(wxCdYyjl);
    }
}
