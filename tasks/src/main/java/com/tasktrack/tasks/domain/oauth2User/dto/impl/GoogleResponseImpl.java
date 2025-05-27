package com.tasktrack.tasks.domain.oauth2User.dto.impl;

import com.tasktrack.tasks.domain.oauth2User.dto.OAuth2Response;

import java.util.Map;

public class GoogleResponseImpl implements OAuth2Response {

    private final Map<String, Object> attributes;

    public GoogleResponseImpl(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
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
