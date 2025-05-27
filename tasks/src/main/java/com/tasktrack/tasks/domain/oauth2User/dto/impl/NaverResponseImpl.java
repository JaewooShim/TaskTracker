package com.tasktrack.tasks.domain.oauth2User.dto.impl;

import com.tasktrack.tasks.domain.oauth2User.dto.OAuth2Response;

import java.util.Map;

public class NaverResponseImpl implements OAuth2Response {

    private final Map<String, Object> attributes;

    public NaverResponseImpl(Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        this.attributes = response;
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}