package com.aigcfast.chat.service.common;

/**
 * 邮箱service
 * @Author lcy
 * @Date 2023/6/1 19:14
 */
public interface EmailService {

    /**
     * 发送邮件信息
     * @param targetEmail 目标邮件地址
     * @param title       标题
     * @param content     内容
     * @author lcy
     * @date 2023/7/12 16:00
     **/
    void sendEmail(String targetEmail,String title,String content);

    /**
     * 发送邮件信息
     * @param targetEmail 目标邮件地址
     * @param title       标题
     * @param content     内容
     * @param bizType     业务类型
     * @author lcy
     * @date 2023/7/12 16:00
     **/
    void sendEmail(String targetEmail,String title,String content,String bizType);

}
