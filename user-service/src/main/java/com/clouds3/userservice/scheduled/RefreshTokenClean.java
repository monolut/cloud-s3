package com.clouds3.userservice.scheduled;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class RefreshTokenClean {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenClean.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RefreshTokenClean(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Scheduled(fixedRate = 60 * 60)
    public void clean() {
        log.info("Refresh token cleanup started");
        jdbcTemplate.update("DELETE FROM refresh_token WHERE revoked = true OR expires_at < now()");
    }
}
