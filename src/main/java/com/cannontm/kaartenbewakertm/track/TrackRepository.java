package com.cannontm.kaartenbewakertm.track;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TrackRepository extends ReactiveCrudRepository<Track, String> {
}
