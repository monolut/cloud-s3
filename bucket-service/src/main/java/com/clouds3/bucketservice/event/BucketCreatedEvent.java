package com.clouds3.bucketservice.event;

import com.clouds3.bucketservice.entity.BucketEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BucketCreatedEvent {

    private final BucketEntity bucket;
}