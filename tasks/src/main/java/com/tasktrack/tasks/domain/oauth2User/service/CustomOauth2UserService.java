package com.tasktrack.tasks.domain.oauth2User.service;

import com.tasktrack.tasks.domain.oauth2User.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.oauth2User.dto.OAuth2Response;
import com.tasktrack.tasks.domain.oauth2User.dto.impl.GithubResponseImpl;
import com.tasktrack.tasks.domain.oauth2User.dto.impl.GoogleResponseImpl;
import com.tasktrack.tasks.domain.oauth2User.dto.impl.NaverResponseImpl;
import com.tasktrack.tasks.domain.oauth2User.entity.UserEntity;
import com.tasktrack.tasks.domain.oauth2User.entity.UserRole;
import com.tasktrack.tasks.domain.oauth2User.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOauth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2Response oAuth2Response = null;

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        switch (registrationId) {
            case "naver" -> oAuth2Response = new NaverResponseImpl(oAuth2User.getAttributes());
            case "google" -> oAuth2Response = new GoogleResponseImpl(oAuth2User.getAttributes());
            case "github" -> oAuth2Response = new GithubResponseImpl(oAuth2User.getAttributes());
            default -> {
                return null;
            }
        }
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        UserEntity userEntity = null;

        if (optionalUser.isPresent()) {
            userEntity = optionalUser.get();
        } else {
            userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setUserRole(UserRole.USER);
        }

        userEntity.setEmail(oAuth2Response.getEmail());
        userEntity.setName(oAuth2Response.getName());
        userRepository.save(userEntity);


        return new CustomOAuth2User(username, oAuth2Response.getName(), userEntity.getUserRole());
    }
}