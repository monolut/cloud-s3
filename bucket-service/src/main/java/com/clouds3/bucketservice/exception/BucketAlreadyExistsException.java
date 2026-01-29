package com.clouds3.bucketservice.exception;

public class BucketAlreadyExistsException extends RuntimeException {

    public BucketAlreadyExistsException(String name, Long ownerId) {
        super("Bucket already exists: name=" + name + ", ownerId=" + ownerId);
    }
}
