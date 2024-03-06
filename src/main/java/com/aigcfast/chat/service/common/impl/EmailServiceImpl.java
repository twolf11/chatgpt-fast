package com.aigcfast.chat.service.common.impl;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.aigcfast.chat.common.constant.Constants;
import com.aigcfast.chat.common.enums.EmailBizTypeEnum;
import com.aigcfast.chat.domain.entity.log.LogEmailSend;
import com.aigcfast.chat.service.common.EmailService;
import com.aigcfast.chat.service.log.LogEmailSendService;
import com.aigcfast.chat.util.Tools;
import com.aigcfast.chat.util.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 邮箱service实现类
 * @Author lcy
 * @Date 2022/5/6 16:20
 */
@Slf4j
@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private MailAccount mailAccount;

    private LogEmailSendService logEmailSendService;

    private ThreadPoolTaskExecutor executor;

    @Override public void sendEmail(String targetEmail,String title,String content){
        sendEmail(targetEmail,title,content,null);
    }

    @Override public void sendEmail(String targetEmail,String title,String content,String bizType){
        LogEmailSend logEmailSend = LogEmailSend.builder()
                .fromEmailAddress(mailAccount.getFrom())
                .toEmailAddress(targetEmail)
                .bizType(EmailBizTypeEnum.valueOf(bizType))
                .requestIp(WebUtil.getIp())
                .content(content)
                .build();
        if (Tools.isNotEmpty(bizType)) {
            logEmailSend.setBizType(EmailBizTypeEnum.valueOf(bizType));
        }
        try {
            String messageId = MailUtil.send(mailAccount,targetEmail,title,content,false);
            logEmailSend.setMessageId(messageId).setStatus(Constants.SUCCESS_STATUS).setMessage("success");
        } catch (Exception e) {
            logEmailSend.setMessage(e.getMessage()).setStatus(Constants.FAIL_STATUS);
        }

        //异步更新邮件
        executor.execute(() -> logEmailSendService.save(logEmailSend));
    }

}
