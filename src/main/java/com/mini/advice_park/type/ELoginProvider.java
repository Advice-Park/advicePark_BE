package com.mini.advice_park.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ELoginProvider {

    DEFAULT("DEFAULT"),
    GOOGLE("GOOGLE");

    private final String name;

    @Override
    public String toString() {
        return this.name;
    }
}
