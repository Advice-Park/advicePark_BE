package com.mini.advice_park.oauth2.domain;


import com.mini.advice_park.user.entity.OAuth2Provider;


public interface UserProvider {

    OAuth2Provider getProvider();

}
