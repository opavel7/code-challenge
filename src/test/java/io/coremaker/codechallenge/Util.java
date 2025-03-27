package io.coremaker.codechallenge;

import org.springframework.web.util.UriComponentsBuilder;

public class Util {
    public static final String LOCALHOST = "http://localhost:8080";
    public static final String USER_ID_HEADER = "USER_ID";

    public static String getCityWeatherUrl(String city) {
        return UriComponentsBuilder.fromUriString("%s/weather".formatted(LOCALHOST))
                .queryParam("city", city)
                .toUriString();
    }
}
