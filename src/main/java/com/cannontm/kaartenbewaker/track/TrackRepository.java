package com.cannontm.kaartenbewaker.track;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends CrudRepository<Track, String> {

    @Query(
            value = """
        SELECT *
        FROM track
        WHERE last_updated + (frequency_mins || ' minutes')::interval < now()
      """,
            nativeQuery = true
    )
    List<Track> findOutdatedTracks();

}
