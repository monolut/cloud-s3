package com.clouds3.bucketservice.service;

import com.clouds3.bucketservice.client.ObjectClient;
import com.clouds3.bucketservice.dto.BucketDto;
import com.clouds3.bucketservice.dto.BucketMetadataDto;
import com.clouds3.bucketservice.dto.BucketStatsDto;
import com.clouds3.bucketservice.entity.BucketEntity;
import com.clouds3.bucketservice.exception.BucketNotFoundException;
import com.clouds3.bucketservice.mapper.BucketMapper;
import com.clouds3.bucketservice.repository.BucketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BucketAdminService {

    private final BucketRepository bucketRepository;
    private final BucketMapper bucketMapper;
    private final ObjectClient objectClient;

    @Autowired
    public BucketAdminService(
            BucketRepository bucketRepository,
            BucketMapper bucketMapper,
            ObjectClient objectClient
    ) {
        this.bucketRepository = bucketRepository;
        this.bucketMapper = bucketMapper;
        this.objectClient = objectClient;
    }

    @Transactional(readOnly = true)
    public List<BucketMetadataDto> getAllBuckets() {
        List<BucketEntity> buckets = bucketRepository.findAll();
        log.info("ADMIN SERVICE | Found {} buckets", buckets.size());
        return buckets.stream()
                .map(bucketMapper::toMetadata)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BucketMetadataDto> getBucketsByUser(Long userId) {
        List<BucketEntity> buckets = bucketRepository.findByOwnerId(userId);
        log.info("ADMIN SERVICE | Found {} buckets for userId={}", buckets.size(), userId);
        return buckets.stream()
                .map(bucketMapper::toMetadata)
                .toList();
    }

    @Transactional(readOnly = true)
    public BucketDto getBucket(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));
        log.info("ADMIN SERVICE | Get bucketId={}", bucketId);
        return bucketMapper.toDto(bucket);
    }

    @Transactional
    public void recalculateMetadata(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        BucketStatsDto bucketStats = objectClient.getBucketStats(bucketId);

        long recalculatedSize = bucketStats.getSize();
        long recalculatedCount = bucketStats.getCount();

        bucket.setSize(recalculatedSize);
        bucket.setObjectCount(recalculatedCount);

        log.info(
                "ADMIN SERVICE | Recalculated metadata for bucketId={} size={} count={}",
                bucketId, recalculatedSize, recalculatedCount
        );
    }

    @Transactional
    public void recalculateAll() {
        List<BucketEntity> buckets = bucketRepository.findAll();
        log.warn("ADMIN SERVICE | Recalculate ALL buckets (count={})", buckets.size());

        for (BucketEntity bucket : buckets) {
            // TODO: заменить на реальный подсчёт
            bucket.setSize(bucket.getSize());
            bucket.setObjectCount(bucket.getObjectCount());
        }

        bucketRepository.saveAll(buckets);
    }

    @Transactional
    public void forceDelete(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        bucketRepository.delete(bucket);
        log.warn("ADMIN SERVICE | Force deleted bucketId={}", bucketId);
    }

    @Transactional
    public void blockBucket(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        bucket.setBlocked(true);
        bucketRepository.save(bucket);

        log.warn("ADMIN SERVICE | Blocked bucketId={}", bucketId);
    }

    @Transactional
    public void unblockBucket(Long bucketId) {
        BucketEntity bucket = bucketRepository.findById(bucketId)
                .orElseThrow(() -> new BucketNotFoundException(bucketId));

        bucket.setBlocked(false);
        bucketRepository.save(bucket);

        log.warn("ADMIN SERVICE | Unblocked bucketId={}", bucketId);
    }
}
