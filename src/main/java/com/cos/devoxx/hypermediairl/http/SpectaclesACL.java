package com.cos.devoxx.hypermediairl.http;

import org.springframework.stereotype.Component;

@Component
public class SpectaclesACL {

    public boolean canView() {
        return true;
    }
}
