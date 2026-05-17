package com.gacha.common.constant;

public interface RedisKeyPattern {
    String SESSION_PREFIX = "qds:sess:";
    String AUTH_TOKEN_BLACKLIST = "auth:token:blacklist:";
    String AUTH_TOKEN_PREFIX = "auth:token:";
}
