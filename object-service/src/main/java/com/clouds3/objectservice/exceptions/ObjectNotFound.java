package com.clouds3.objectservice.exceptions;

public class ObjectNotFound extends RuntimeException {
    public ObjectNotFound(String message) {
        super(message);
    }

    public static ObjectNotFound byObjectKey(String objectKey) {
        return new ObjectNotFound(String.format("Object not found with object key %s", objectKey));
    }
}
