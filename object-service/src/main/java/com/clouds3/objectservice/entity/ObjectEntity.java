package com.clouds3.objectservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "objects")
@Builder
public class ObjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "object_key", unique = true)
    private String objectKey;

    @Column(nullable = false, name = "bucket_id")
    private Long bucketId;

    @Column(nullable = false, name = "owner_id")
    private Long ownerId;

    @Column(nullable = false, name = "original_file_name")
    private String originalFileName;

    @Column(nullable = false, name = "content_type")
    private String contentType;

    @Column(nullable = false, name = "size")
    private Long size;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;
}
