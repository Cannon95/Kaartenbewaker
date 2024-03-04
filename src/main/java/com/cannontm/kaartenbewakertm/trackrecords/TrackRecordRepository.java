package com.cannontm.kaartenbewakertm.trackrecords;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface TrackRecordRepository extends ReactiveCrudRepository<TrackRecord, String> {
}
