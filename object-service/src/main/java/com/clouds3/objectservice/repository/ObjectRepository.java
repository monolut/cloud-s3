package com.clouds3.objectservice.repository;

import com.clouds3.objectservice.dto.BucketStatsDto;
import com.clouds3.objectservice.entity.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectEntity, Long> {

    Optional<ObjectEntity> findByObjectKey(String objectKey);

    Optional<ObjectEntity> findByBucketIdAndObjectKey(Long bucketId, String objectKey);

    List<ObjectEntity> findByBucketId(Long bucketId);

    @Query("""
           SELECT new com.clouds3.bucketservice.dto.BucketStatsDto(
                COUNT(o),
                COALESCE(SUM(o.size),0)
           )
           FROM ObjectEntity o
           WHERE o.bucketId = :bucketId
           """)
    BucketStatsDto getBucketStats(Long bucketId);
}
