package com.clouds3.objectservice.repository;

import com.clouds3.objectservice.entity.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectEntity, Long> {

    Optional<ObjectEntity> findByObjectKey(String objectKey);

    Optional<ObjectEntity> findByBucketIdAndObjectKey(Long bucketId, String objectKey);

    List<ObjectEntity> findByBucketId(Long bucketId);
}
