package com.cannontm.kaartenbewaker.auth;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken, Integer> {
}
