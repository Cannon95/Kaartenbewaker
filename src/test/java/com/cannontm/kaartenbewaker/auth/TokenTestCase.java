package com.cannontm.kaartenbewaker.auth;

import com.cannontm.kaartenbewaker.dto.JobType;

public record TokenTestCase(String name, AuthToken authToken, JobType jobType) {
}
