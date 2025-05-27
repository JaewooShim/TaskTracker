package com.tasktrack.tasks.domain.oauth2User.dto.impl;

import com.tasktrack.tasks.domain.oauth2User.dto.OAuth2Response;

import java.util.Map;

public class GithubResponseImpl implements OAuth2Response {

    private final Map<String, Object> attributes;

    public GithubResponseImpl(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProvider() {
        return "github";
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getEmail() {
        if ((String) attributes.get("email") == null) {

        }
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
