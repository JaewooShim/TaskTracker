package com.tasktrack.tasks.domain.auth.repository;

import com.tasktrack.tasks.domain.auth.service.SocialClientRegistration;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
public class CustomClientRegistrationRepo {

    private final SocialClientRegistration socialClientRegistration;

    public CustomClientRegistrationRepo(SocialClientRegistration socialClientRegistration) {
        this.socialClientRegistration = socialClientRegistration;
    }

    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(socialClientRegistration.naverClientRegistration(),
                socialClientRegistration.googleClientRegistration(),
                socialClientRegistration.githubClientRegistration());
    }
}
