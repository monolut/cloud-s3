package com.clouds3.bucketservice.exception;

public class BucketNotFoundException extends RuntimeException {

    public BucketNotFoundException(Long bucketId) {
        super("Bucket not found: " + bucketId);
    }

    public BucketNotFoundException(String name, Long ownerId) {
        super("Bucket not found: name=" + name + ", ownerId=" + ownerId);
    }
}
