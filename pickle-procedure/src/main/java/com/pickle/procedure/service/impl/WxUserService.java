package com.pickle.procedure.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.util.StringUtil;
import com.pickle.procedure.bean.WxUser;
import com.pickle.procedure.mapper.WxUserMapper;
import com.pickle.procedure.service.IWxUserService;
import com.pickle.sys.bean.GgFj;
import com.pickle.sys.service.IGgFjService;
import com.pickle.utils.base.BaseService;
import com.pickle.utils.constant.StringConstant;
import com.pickle.utils.date.DateUtils;
import com.pickle.utils.exception.BizException;
import com.pickle.utils.jwt.JwtUtil;
import com.pickle.utils.redis.RedisCacheService;
import com.pickle.utils.uuid.UUIDUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WxUserService extends BaseService<WxUser> implements IWxUserService {
    private final WxUserMapper wxUserMapper;
    private final RedisCacheService redisCacheService;
    private final JwtUtil jwtUtil;
    private final IGgFjService ggFjService;

    @Value("${wx.miniapp.appid:}")
    private String appId;

    @Value("${wx.miniapp.secret:}")
    private String secret;

    private static final Date date = new Date();
    private static final String filePath = System.getProperty("user.dir") +"\\file\\" + DateUtils.getYear(date) +"\\" +DateUtils.getMonth(date);


    @Override
    public WxUser login(WxUser sysUser) {
        // 1. 调用微信接口，用 code 换取 openId 和 sessionKey
        String wxUrl = String.format(
                "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                appId, secret, sysUser.getWxCode()
        );

        String result = HttpUtil.get(wxUrl);
        log.info("微信接口返回: {}", result);

        JSONObject jsonObject = JSONUtil.parseObj(result);
        String openId = jsonObject.getStr("openid");
        String sessionKey = jsonObject.getStr("session_key");
        String errCode = jsonObject.getStr("errcode");

        if (StrUtil.isNotBlank(errCode) && !"0".equals(errCode)) {
            log.error("微信登录失败，errcode: {}, errmsg: {}", errCode, jsonObject.getStr("errmsg"));
            throw new RuntimeException("微信登录失败: " + jsonObject.getStr("errmsg"));
        }

        if (StrUtil.isBlank(openId)) {
            throw new RuntimeException("获取openId失败");
        }

        // 2. 查询数据库是否存在该用户
        WxUser wxUser = new WxUser();
        wxUser.setOpenid(openId);
        List<WxUser> wxUserList = wxUserMapper.selectListByBean(wxUser);

        // 3. 不存在则创建新用户
        if (wxUserList.isEmpty()) {
            wxUser.setUserUuid(UUIDUtil.newUUID());
            wxUser.setWxCode(sysUser.getWxCode());
            wxUser.setSessionKey(sessionKey);
            wxUserMapper.insert(wxUser);
            log.info("创建新用户，openId: {}", openId);
        }else {
            wxUser = wxUserList.get(0);
        }

        // 4. 生成 JWT token
        String token = jwtUtil.generateToken(openId, wxUser.getUserUuid());

        // 5. 返回用户信息
        wxUser.setToken(token);
        return wxUser;
    }

    @Override
    public WxUser uploadAvatar(MultipartFile file, WxUser wxUser) {
        String filename = file.getOriginalFilename();
        if (StringUtil.isEmpty(filename)){
            throw new BizException("文件名为空");
        }
        String uuid = UUIDUtil.newUUID();
        String[] split = filename.split(StringConstant.POINT);

        String wjLj = filePath + "\\" + uuid +StringConstant.HYPHEN_LINE +filename;
        try {
            if (FileUtil.isDirectory(filePath)){
                FileUtil.mkdir(filePath);
            }
            FileUtil.writeBytes(file.getBytes(), wjLj);
            log.info("文件存放在" +filePath +"下");
        } catch (IOException e) {
            e.printStackTrace();
        }

        GgFj ggFj = new GgFj();
        ggFj.setFjUuid(uuid);
        ggFj.setFjMc(filename);
        ggFj.setFjLj(wjLj);
        ggFj.setWjDx(Integer.parseInt(String.valueOf(file.getSize())));
        ggFj.setWjGs(split[1]);
        ggFj.setScFs("01");
        ggFjService.save(ggFj);

        wxUser.setUserImage(wjLj);
        wxUserMapper.updateByPrimaryKeySelective(wxUser);
        return wxUser;
    }
}