package com.aigcfast.chat.service.blackList;

import com.aigcfast.chat.util.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * 黑名单中用户处理
 */
@Service
public class BlackListService {

    private static  final String BLACK_LIST_KEY = "blackList";

    private static  final String BLACK_IP_LIST_KEY = "blackIpList";


    @Autowired
    private RedisService redisService;

    /**
     * 检查是否为黑名单账户
     * @param account
     * @return
     */
    public boolean check(Object account){
        List<Object> forList = redisService.getForList(BLACK_LIST_KEY);
        if(forList == null)
            return  false;
        return forList.contains(account);
    }

    /**
     * 增加黑名单ip
     * @param ip
     * @return
     */
    public void addBlackIp(Object ip){
        List<Object> forList = redisService.getForList(BLACK_IP_LIST_KEY);
        if (!forList.contains(ip)) {
            redisService.rightPush(BLACK_IP_LIST_KEY,ip);
        }
    }

    /**
     * 检查是否为黑名单ip
     * @param ip
     * @return
     */
    public boolean checkIp(Object ip){
        List<Object> forList = redisService.getForList(BLACK_IP_LIST_KEY);
        if(forList == null)
            return  false;
        return forList.contains(ip);
    }
}
