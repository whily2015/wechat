package com.hyde.wmis.job;

import com.hyde.wmis.cont.Dict;
import com.hyde.wmis.entity.WmDictionary;
import com.hyde.wmis.entity.WmPublicAccount;
import com.hyde.wmis.service.WmDictionaryService;
import com.hyde.wmis.service.WmPublicAccountService;
import com.hyde.wmis.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Order(value = 1)
@Service
public class RefreshWxToken implements ApplicationRunner {

    @Autowired
    private WmDictionaryService wmDictionaryService;
    @Autowired
    private WmPublicAccountService wmPublicAccountService;

    private final Logger logger = LoggerFactory.getLogger(RefreshWxToken.class);

//    @Scheduled(cron = "0 50 0/1 * * *")
    @Scheduled(cron = "0/10 * * * * *")
    public void refreshWxToken() {
        try {
            logger.info("refreshWxToken start");
            List<WmPublicAccount> publicAccounts = wmPublicAccountService.findAll();
            for (WmPublicAccount publicAccount : publicAccounts) {
                String weChatNo = publicAccount.getWechatNo();
                String appId = publicAccount.getAppId();
                String secret = publicAccount.getSecret();
                logger.info("refreshWxToken " + weChatNo + " Starting");
                WmDictionary dictionary = wmDictionaryService.findWmDictionary(weChatNo, Dict.ACCESS_TOKEN);
                if (dictionary == null) {
                    dictionary = new WmDictionary();
                    dictionary.setDictType(weChatNo);
                    dictionary.setDictKey(Dict.ACCESS_TOKEN);
                    dictionary.setCreateTime(DateUtil.getDate());
                }
                String accessToken = "";//WxUtil.getAccessToken(appId, secret);
                dictionary.setDictValue(accessToken);
                wmDictionaryService.saveWmDictionary(dictionary);
                logger.info("refreshWxToken " + weChatNo + " Successful");
            }
            logger.info("refreshWxToken end");
        } catch (Exception e) {
            logger.error("refreshWxToken exception: ", e);
        }

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        refreshWxToken();
    }
}
