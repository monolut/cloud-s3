package com.clouds3.bucketservice.listener;

import com.clouds3.bucketservice.event.BucketCreatedEvent;
import com.clouds3.bucketservice.event.BucketDeletedEvent;
import com.clouds3.bucketservice.event.BucketUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BucketEventListener {

    @Async("applicationTaskExecutor")
    @EventListener
    public void handleBucketCreated(BucketCreatedEvent event) {
        log.info("EVENT | Bucket created: id={}, owner={}",
                event.getBucket().getId(), event.getBucket().getOwnerId());
    }

    @Async("applicationTaskExecutor")
    @EventListener
    public void handleBucketUpdated(BucketUpdatedEvent event) {
        log.info("EVENT | Bucket updated: id={}, owner={}",
                event.getBucket().getId(), event.getBucket().getOwnerId());
    }

    @Async("applicationTaskExecutor")
    @EventListener
    public void handleBucketDeleted(BucketDeletedEvent event) {
        log.info("EVENT | Bucket deleted: id={}, owner={}",
                event.getBucketId(), event.getOwnerId());
    }
}
