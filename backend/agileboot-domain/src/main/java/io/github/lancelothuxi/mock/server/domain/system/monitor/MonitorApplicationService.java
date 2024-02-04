package io.github.lancelothuxi.mock.server.domain.system.monitor;

import cn.hutool.core.util.StrUtil;
import io.github.lancelothuxi.mock.server.common.exception.ApiException;
import io.github.lancelothuxi.mock.server.domain.common.cache.CacheCenter;
import io.github.lancelothuxi.mock.server.domain.system.monitor.dto.OnlineUserDTO;
import io.github.lancelothuxi.mock.server.domain.system.monitor.dto.RedisCacheInfoDTO;
import io.github.lancelothuxi.mock.server.domain.system.monitor.dto.RedisCacheInfoDTO.CommandStatusDTO;
import io.github.lancelothuxi.mock.server.domain.system.monitor.dto.ServerInfo;
import io.github.lancelothuxi.mock.server.infrastructure.cache.redis.CacheKeyEnum;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.github.lancelothuxi.mock.server.common.exception.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @author valarchie
 */
@Service
@RequiredArgsConstructor
public class MonitorApplicationService {

    private final RedisTemplate<String, ?> redisTemplate;

    public RedisCacheInfoDTO getRedisCacheInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute(
            (RedisCallback<Object>) connection -> connection.info("commandstats"));
        Long dbSize = redisTemplate.execute(RedisServerCommands::dbSize);

        if (commandStats == null || info == null) {
            throw new ApiException(ErrorCode.Internal.INTERNAL_ERROR, "获取Redis监控信息失败。");
        }

        RedisCacheInfoDTO cacheInfo = new RedisCacheInfoDTO();

        cacheInfo.setInfo(info);
        cacheInfo.setDbSize(dbSize);
        cacheInfo.setCommandStats(new ArrayList<>());

        commandStats.stringPropertyNames().forEach(key -> {
            String property = commandStats.getProperty(key);

            CommandStatusDTO commonStatus = new CommandStatusDTO();
            commonStatus.setName(StrUtil.removePrefix(key, "cmdstat_"));
            commonStatus.setValue(StrUtil.subBetween(property, "calls=", ",usec"));

            cacheInfo.getCommandStats().add(commonStatus);
        });

        return cacheInfo;
    }

    public List<OnlineUserDTO> getOnlineUserList(String username, String ipAddress) {
        Collection<String> keys = redisTemplate.keys(CacheKeyEnum.LOGIN_USER_KEY.key() + "*");

        Stream<OnlineUserDTO> onlineUserStream = keys.stream().map(o ->
                    CacheCenter.loginUserCache.getObjectOnlyInCacheByKey(o))
            .filter(Objects::nonNull).map(OnlineUserDTO::new);

        List<OnlineUserDTO> filteredOnlineUsers = onlineUserStream
            .filter(o ->
                StrUtil.isEmpty(username) || username.equals(o.getUsername())
            ).filter( o ->
                StrUtil.isEmpty(ipAddress) || ipAddress.equals(o.getIpAddress())
            ).collect(Collectors.toList());

        Collections.reverse(filteredOnlineUsers);
        return filteredOnlineUsers;
    }

    public ServerInfo getServerInfo() {
        return ServerInfo.fillInfo();
    }


}
