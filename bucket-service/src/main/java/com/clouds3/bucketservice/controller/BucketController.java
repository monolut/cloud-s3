package com.clouds3.bucketservice.controller;

import com.clouds3.bucketservice.dto.BucketDto;
import com.clouds3.bucketservice.enums.BucketType;
import com.clouds3.bucketservice.service.BucketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/buckets")
@CrossOrigin("*")
public class BucketController {

    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BucketDto> createBucket(
            @RequestParam Long ownerId,
            @RequestParam String name,
            @RequestParam BucketType type
    ) {

        log.info("POST /buckets - ownerId={}, name={}, type={}",
                ownerId, name, type);

        BucketDto created =  bucketService.createBucket(ownerId, name, type);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{bucketId}")
    public ResponseEntity<BucketDto> getBucket(
            @PathVariable Long bucketId
    ) {

        log.info("GET /buckets recieved for bucketId={}",
                bucketId);

        BucketDto bucket = bucketService.getBucket(bucketId);
        return ResponseEntity.ok(bucket);
    }

    @GetMapping
    public ResponseEntity<List<BucketDto>> getBuckets() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long ownerId = Optional.ofNullable(authentication)
                        .map(Authentication::getPrincipal)
                        .map(Object::toString)
                        .map(Long::valueOf)
                        .orElseThrow(() -> new RuntimeException("User is not authenticated"));

        log.info("GET /buckets?ownerId={}",
                ownerId);

        List<BucketDto> buckets = bucketService.getBucketsByOwner(ownerId);
        return ResponseEntity.ok(buckets);
    }

    @PatchMapping("/{bucketId}")
    public ResponseEntity<BucketDto> updateBucket(
            @PathVariable Long bucketId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BucketType type
    ) {

        log.info("PATCH /buckets recieved for bukcetId={} - newName={}, newType={}",
                bucketId, name, type);

        BucketDto updated = bucketService.updateBucket(bucketId, name, type);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{bucketId}")
    public ResponseEntity<Void> deleteBucket(
            @PathVariable Long bucketId
    ) {

        log.info("DELETE /buckets recieved for bucketId={}",
                bucketId);

        bucketService.deleteBucket(bucketId);
        return ResponseEntity.noContent().build();
    }
}
