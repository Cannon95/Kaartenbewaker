package com.cannontm.kaartenbewaker.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AuthTokenService {

    private final AuthTokenRepository authTokenRepository;

    public void save(AuthToken authToken){
        if(authToken != null){
            authTokenRepository.save(authToken);
        }
        else throw new IllegalArgumentException("token should not be empty");

    }

    public AuthToken getToken(){
        if(authTokenRepository.findById(0).isPresent()){
            return authTokenRepository.findById(0).get();
        }
        else return null;
    }
}
