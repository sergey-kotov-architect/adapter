package com.sergeykotov.adapter.service;

import com.sergeykotov.adapter.exception.AuthorizationException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {
    public void authorize(String authorization) {
        if (authorization == null) {
            throw new AuthorizationException();
        }
        //TODO: verify authorization header
    }
}