package com.jyblog.monitor.offline.service.impl;

import com.jyblog.config.JyJWTConfig;
import com.jyblog.domain.UserCacheInfo;
import com.jyblog.monitor.offline.model.vo.UserQueryVO;
import com.jyblog.monitor.offline.service.OfflineService;
import com.jyblog.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author LGX_TvT <br>
 * @version 1.0 <br>
 * Create by 2022-05-13 22:40 <br>
 * @description: OfflineServiceImpl <br>
 */
@Service
public class OfflineServiceImpl implements OfflineService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private JyJWTConfig jyJWTConfig;

    @Override
    public List<UserCacheInfo> getList(UserQueryVO vo) {
        Set<String> keys = redisTemplate.keys(jyJWTConfig.getLoginUserKey() + ":*");
        List<UserCacheInfo> record = new ArrayList<>();
        keys.stream().map(x -> (UserCacheInfo) redisUtil.getValue(x)).forEach(x -> record.add(x));
        return record.stream().filter(x ->
                !StringUtils.isNotBlank(vo.getUsername()) || x.getUsername().contains(vo.getUsername())
        ).collect(Collectors.toList());
    }

    @Override
    public void forcedOffline(String username) {
        String key = jyJWTConfig.getLoginUserKey() + ":" + username;
        if (redisUtil.exists(key)) {
            redisUtil.delete(key);
        }
    }
}
