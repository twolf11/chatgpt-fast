
package com.aigcfast.chat.service.log.impl;

import org.springframework.stereotype.Service;

import com.aigcfast.chat.domain.entity.log.LogEmailSend;
import com.aigcfast.chat.mapper.log.LogEmailSendMapper;
import com.aigcfast.chat.service.log.LogEmailSendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
* 邮箱发送日志service实现
* @Author lcy
* @Date 2023-07-12
*/
@Service
@AllArgsConstructor
@Slf4j
public class LogEmailSendServiceImpl extends ServiceImpl<LogEmailSendMapper,LogEmailSend> implements LogEmailSendService {


}
