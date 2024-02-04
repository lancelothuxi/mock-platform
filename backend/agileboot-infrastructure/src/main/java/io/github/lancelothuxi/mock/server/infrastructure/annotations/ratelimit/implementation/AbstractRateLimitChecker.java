package io.github.lancelothuxi.mock.server.infrastructure.annotations.ratelimit.implementation;

import io.github.lancelothuxi.mock.server.infrastructure.annotations.ratelimit.RateLimit;

/**
 * @author valarchie
 */
public abstract class AbstractRateLimitChecker {

    /**
     * 检查是否超出限流
     *
     * @param rateLimiter RateLimit
     */
    public abstract void check(RateLimit rateLimiter);

}
