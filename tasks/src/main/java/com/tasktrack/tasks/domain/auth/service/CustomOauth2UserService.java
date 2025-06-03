package com.tasktrack.tasks.domain.auth.service;

import com.tasktrack.tasks.domain.auth.dto.OAuth2UserInfo;
import com.tasktrack.tasks.domain.auth.dto.CustomOAuth2User;
import com.tasktrack.tasks.domain.auth.entity.UserEntity;
import com.tasktrack.tasks.domain.auth.repository.UserRepository;
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

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(registrationId, oAuth2User.getAttributes());

        UserEntity userEntity = userRepository.findByUsername(oAuth2UserInfo.username())
                .orElseGet(oAuth2UserInfo::toEntity);
        userRepository.save(userEntity);

        return new CustomOAuth2User(userEntity, oAuth2UserInfo.attributes());
    }
}