package io.coremaker.codechallenge.exception;

public class CityNotFoundException extends NotFoundException {
    public CityNotFoundException(String city) {
        super("City %s not found".formatted(city));
    }
}
