package com.clouds3.bucketservice.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BucketDeletedEvent {

    private final Long bucketId;
    private final Long ownerId;
}
