package com.clouds3.bucketservice.service;

import com.clouds3.bucketservice.dto.BucketDto;
import com.clouds3.bucketservice.entity.BucketEntity;
import com.clouds3.bucketservice.enums.BucketType;
import com.clouds3.bucketservice.event.BucketCreatedEvent;
import com.clouds3.bucketservice.event.BucketDeletedEvent;
import com.clouds3.bucketservice.event.BucketUpdatedEvent;
import com.clouds3.bucketservice.exception.BucketAlreadyExistsException;
import com.clouds3.bucketservice.exception.BucketNotFoundException;
import com.clouds3.bucketservice.mapper.BucketMapper;
import com.clouds3.bucketservice.repository.BucketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BucketService {

    private final BucketRepository bucketRepository;
    private final BucketMapper bucketMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public BucketService(
            BucketRepository bucketRepository,
            BucketMapper bucketMapper,
            ApplicationEventPublisher applicationEventPublisher
    ) {
        this.bucketRepository = bucketRepository;
        this.bucketMapper = bucketMapper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public BucketDto createBucket(Long ownerId, String name, BucketType type) {
        if(bucketRepository.existsByNameAndOwnerId(name, ownerId)) {

            log.warn("Bucket creation failed: bucket {} already exists for owner {}", name, ownerId);

            throw new BucketAlreadyExistsException(name, ownerId);
        }

        BucketEntity bucket = BucketEntity.builder()
                .name(name)
                .ownerId(ownerId)
                .type(type)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .size(0L)
                .objectCount(0L)
                .blocked(false)
                .build();

        BucketEntity saved = bucketRepository.save(bucket);
        applicationEventPublisher.publishEvent(new BucketCreatedEvent(bucket));

        log.info("Bucket created: {} by owner {}", saved.getName(), ownerId);

        return bucketMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public BucketDto getBucket(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        ensureNotBlocked(bucket);

        log.info("Fetched bucket {} for owner {}", bucket.getName(), bucket.getOwnerId());
        return bucketMapper.toDto(bucket);
    }

    @Transactional(readOnly = true)
    public List<BucketDto> getBucketsByOwner(Long ownerId) {
        List<BucketEntity> buckets = bucketRepository.findByOwnerId(ownerId);

        log.info("Fetched {} buckets for owner {}", buckets.size(), ownerId);
        return bucketMapper.toDtoList(buckets);
    }

    @Transactional
    public BucketDto updateBucket(Long bucketId, String newName, BucketType newType) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        ensureNotBlocked(bucket);

        if(newName != null && !newName.isBlank()) {
            bucket.setName(newName);
        }

        if(newType != null) {
            bucket.setType(newType);
        }

        bucket.setUpdatedAt(LocalDateTime.now());
        BucketEntity updated = bucketRepository.save(bucket);
        applicationEventPublisher.publishEvent(new BucketUpdatedEvent(bucket));

        log.info("Bucket updated: {} by owner {}", updated.getName(), updated.getOwnerId());
        return bucketMapper.toDto(updated);
    }

    @Transactional
    public void deleteBucket(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        ensureNotBlocked(bucket);

        bucketRepository.delete(bucket);
        applicationEventPublisher.publishEvent(new BucketDeletedEvent(bucketId, bucket.getOwnerId()));
        log.info("Bucket deleted: {} by owner {}", bucket.getName(), bucket.getOwnerId());
    }

    private void ensureNotBlocked(BucketEntity bucket) {
        if(bucket.isBlocked()) {
            log.warn(
                    "Access denied: bucket {} is blocked (ownerId={})",
                    bucket.getId(),
                    bucket.getOwnerId()
            );
            throw new IllegalStateException("Bucket is blocked");
        }
    }
}
