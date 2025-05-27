package com.tasktrack.tasks.domain.oauth2.service;

import com.tasktrack.tasks.domain.oauth2.dto.OAuth2Response;
import com.tasktrack.tasks.domain.oauth2.dto.impl.GithubResponseImpl;
import com.tasktrack.tasks.domain.oauth2.dto.impl.GoogleResponseImpl;
import com.tasktrack.tasks.domain.oauth2.dto.impl.NaverResponseImpl;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Response oAuth2Response = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponseImpl(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponseImpl(oAuth2User.getAttributes());
        } else if (registrationId.equals("github")){
            oAuth2Response = new GithubResponseImpl(oAuth2User.getAttributes());
        } else return null;
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        return new CustomOAuth2User();
    }
}