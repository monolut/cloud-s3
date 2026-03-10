package com.clouds3.objectservice.controller;

import com.clouds3.objectservice.dto.ObjectResponseDto;
import com.clouds3.objectservice.service.ObjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/buckets")
public class ObjectController {

    private final ObjectService objectService;

    @Autowired
    public ObjectController(ObjectService objectService) {
        this.objectService = objectService;
    }

    @GetMapping("/{bucketId}/objects/{objectKey}/metadata")
    public ResponseEntity<ObjectResponseDto> getObjectByObjectKey(
            @PathVariable Long bucketId,
            @PathVariable String objectKey
    ) {

        log.info("GET /{bucketId}/objects/{objectKey}/metadata request received for bucketid={} objectKey={}",
                bucketId, objectKey);

        ObjectResponseDto object = objectService.getObjectByObjectKey(bucketId, objectKey);
        return ResponseEntity.ok(object);
    }

    @GetMapping("/{bucketId}")
    public ResponseEntity<List<ObjectResponseDto>> getObjectsByBucketId(
            @PathVariable Long bucketId
    ) {

        log.info("GET /{bucketId} request received for bucketid={} ",
                bucketId);

        List<ObjectResponseDto> objects = objectService.getObjectsByBucketId(bucketId);
        return ResponseEntity.ok(objects);
    }

    @DeleteMapping("/{bucketId}/objects/{objectKey}")
    public ResponseEntity<Void> deleteObjectByObjectKey(
            @PathVariable Long bucketId,
            @PathVariable String objectKey
    ) {

        log.info("DELETE /{bucketId}/objects/{objectKey} request received for bucketid={} objectKey={}",
                bucketId, objectKey);

        objectService.deleteObjectByObjectKey(bucketId, objectKey);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{bucketId}/objects/{objectKey}")
    public ResponseEntity<ObjectResponseDto> uploadObject(
            @PathVariable Long bucketId,
            @PathVariable String objectKey,
            @RequestParam MultipartFile file
    ) {

        log.info("POST /{bucketId}/objects/{objectKey} request received for bucketid={} objectKey={}",
                bucketId, objectKey);


        ObjectResponseDto object = objectService.uploadObject(bucketId, objectKey, file);
        return ResponseEntity.ok(object);
    }

    @GetMapping("/{bucketId}/objects/{objectKey}/download")
    public ResponseEntity<Resource> downloadObject(
            @PathVariable Long bucketId,
            @PathVariable String objectKey
    ) throws IOException {

        log.info("GET /{bucketId}/objects/{objectKey}/download request received for bucketid={} objectKey={}",
                bucketId, objectKey);

        Path filePath = objectService.downloadObject(bucketId, objectKey);

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + objectKey + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
