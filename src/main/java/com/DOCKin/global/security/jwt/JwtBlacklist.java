package com.DOCKin.global.security.jwt;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtBlacklist {
    private final Map<String,Long> blacklist = new ConcurrentHashMap<>();

    @Scheduled(fixedDelay = 3600000)
    public void cleanup(){
        long now = System.currentTimeMillis();
        blacklist.entrySet().removeIf(entry->entry.getValue()<now);
    }

    public void add(String token,Long expiration){
        blacklist.put(token,expiration);
    }

    public boolean isBlacklisted(String token){
        return blacklist.containsKey(token);
    }

}
