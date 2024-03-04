package com.cannontm.kaartenbewakertm.trackrecords;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrackrecordService {

    private TrackRecordRepository trackRecordRepository;

    public void addRecords(List<TrackRecord> trlist){
        if(trlist != null && !trlist.isEmpty()){
            trackRecordRepository.saveAll(trlist);
        }

    }
}
