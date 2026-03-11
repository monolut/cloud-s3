package com.clouds3.objectservice.service;

import com.clouds3.objectservice.client.BucketClient;
import com.clouds3.objectservice.dto.BucketDto;
import com.clouds3.objectservice.dto.ObjectResponseDto;
import com.clouds3.objectservice.entity.ObjectEntity;
import com.clouds3.objectservice.exceptions.ObjectNotFound;
import com.clouds3.objectservice.mapper.ObjectMapper;
import com.clouds3.objectservice.repository.ObjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ObjectService {

    private final ObjectMapper objectMapper;
    private final ObjectRepository objectRepository;
    private final BucketClient bucketClient;

    private final Path storageRoot;

    public ObjectService(
            ObjectMapper objectMapper,
            ObjectRepository objectRepository,
            BucketClient bucketClient,
            @Value("${STORAGE_BASE_PATH}") String storageBasePath
    ) {
        this.objectMapper = objectMapper;
        this.objectRepository = objectRepository;
        this.bucketClient = bucketClient;
        this.storageRoot = Paths.get(storageBasePath);
        initStorage();
    }

    private void initStorage() {
        try {
            if (!Files.exists(storageRoot)) {
                Files.createDirectories(storageRoot);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create storage directory", e);
        }
    }

    @Transactional(readOnly = true)
    public ObjectResponseDto getObjectByObjectKey(Long bucketId, String objectKey) {

        log.debug("Fetching object DTO by objectKey={}", objectKey);

        ObjectEntity objectEntity = objectRepository.findByBucketIdAndObjectKey(bucketId, objectKey)
                .orElseThrow(() -> {
                    log.warn("Object not found by objectKey={}", objectKey);
                    return ObjectNotFound.byObjectKey(objectKey);
                });

        return objectMapper.toResponseDto(objectEntity);
    }

    @Transactional(readOnly = true)
    public List<ObjectResponseDto> getObjectsByBucketId(Long bucketId) {

        log.debug("Fetching objects response DTO by bucketId={}", bucketId);

        validateBucket(bucketId);

        List<ObjectResponseDto> objects = objectRepository.findByBucketId(bucketId).stream()
                .map(objectMapper::toResponseDto)
                .collect(Collectors.toList());

        log.debug("Fetched {} objects with bucketId={}", objects.size(), bucketId);

        return objects;
    }

    @Transactional
    public void deleteObjectByObjectKey(
            Long bucketId,
            String objectKey
    ) {

        log.info("Deleting object with objectKey={} bucketId={}",
                objectKey, bucketId);

        ObjectEntity objectEntity = objectRepository.findByBucketIdAndObjectKey(bucketId, objectKey)
                .orElseThrow(() -> ObjectNotFound.byObjectKey(objectKey));

        Path filePath = getObjectPath(bucketId, objectEntity.getObjectKey());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Failed to delete file: {}", filePath, e);
        }

        objectRepository.delete(objectEntity);

        log.info("Object successfully deleted, objectKey={} and bucketId={}", objectKey, bucketId);
    }

    @Transactional
    public ObjectResponseDto uploadObject(
            Long bucketId,
            String objectKey,
            MultipartFile file
    ) {

        log.debug("Upload object with bucketId={} objectKey={} file={}",
                bucketId, objectKey, file);

        validateBucket(bucketId);

        Path bucketDir = storageRoot.resolve(bucketId.toString());
        try {
            if (!Files.exists(bucketDir)) {
                Files.createDirectories(bucketDir);
            }

            Path filePath = bucketDir.resolve(objectKey);
            file.transferTo(filePath.toFile());
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long ownerId = Optional.ofNullable(authentication)
                .map(Authentication::getPrincipal)
                .map(Object::toString)
                .map(Long::valueOf)
                .orElseThrow(() -> new RuntimeException("User is not authenticated"));

        ObjectEntity objectEntity = new ObjectEntity();
        objectEntity.setObjectKey(objectKey);
        objectEntity.setBucketId(bucketId);
        objectEntity.setOwnerId(ownerId);
        objectEntity.setOriginalFileName(file.getName());
        objectEntity.setContentType(file.getContentType());
        objectEntity.setSize(file.getSize());
        objectEntity.setCreatedAt(LocalDateTime.now());

        objectRepository.save(objectEntity);

        log.info("Object successfully uploaded with bucketId={} objectKey={} file={}",
                bucketId, objectKey, file);

        return objectMapper.toResponseDto(objectEntity);
    }

    @Transactional(readOnly = true)
    public Path downloadObject(
            Long bucketId,
            String objectKey
    ) {

        log.debug("Downloading object bucketId={} objectKey={}",
                bucketId, objectKey);

        ObjectEntity object = objectRepository.findByBucketIdAndObjectKey(bucketId, objectKey)
                .orElseThrow(() -> ObjectNotFound.byObjectKey(objectKey));

        Path filePath = getObjectPath(bucketId, objectKey);

        if (!Files.exists(filePath)) {
            throw new RuntimeException("File not found in storage");
        }

        return filePath;
    }

    private Path getObjectPath(
            Long bucketId,
            String objectKey
    ) {
        return storageRoot.resolve(bucketId.toString()).resolve(objectKey);
    }

    private void validateBucket(
            Long bucketId
    ) {

        BucketDto bucket = bucketClient.getBucket(bucketId);

        if(bucket == null) {
            log.warn("Bucket not found with bucketId={}", bucketId);
            throw new RuntimeException("Bucket not found");
        }
    }
}