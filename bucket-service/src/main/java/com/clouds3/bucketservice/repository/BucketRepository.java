package com.clouds3.bucketservice.repository;

import com.clouds3.bucketservice.entity.BucketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BucketRepository extends JpaRepository<BucketEntity, Long> {

    List<BucketEntity> findByOwnerId(Long ownerId);

    Optional<BucketEntity> findByNameAndOwnerId(String name, Long ownerId);

    boolean existsByNameAndOwnerId(String name, Long ownerId);
}
