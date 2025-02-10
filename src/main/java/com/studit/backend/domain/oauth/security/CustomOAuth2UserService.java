package com.studit.backend.domain.oauth.security;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // 인증된 OAuth2User 정보 가져오기
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2AuthenticationToken authentication =
                new OAuth2AuthenticationToken(oauth2User, oauth2User.getAuthorities(), registrationId);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return oauth2User;
    }
}