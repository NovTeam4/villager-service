package com.example.villagerservice.config.security.oauth2.converters;

public interface ProviderUserConverter<T, R> {
    R converter(T t);
}
