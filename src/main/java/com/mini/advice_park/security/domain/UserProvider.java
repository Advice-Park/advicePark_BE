package com.mini.advice_park.security.domain;


import com.mini.advice_park.member.domain.OAuth2Provider;

public interface UserProvider {

    OAuth2Provider getProvider();

}
