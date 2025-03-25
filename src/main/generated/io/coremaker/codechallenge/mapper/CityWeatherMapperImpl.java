package io.coremaker.codechallenge.mapper;

import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherResponse;
import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-25T11:01:51+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.7 (Amazon.com Inc.)"
)
@Component
public class CityWeatherMapperImpl implements CityWeatherMapper {

    @Override
    public CityWeatherDTO toCityWeatherDTO(CityWeatherResponse cityWeatherResponse) {
        if ( cityWeatherResponse == null ) {
            return null;
        }

        CityWeatherDTO.CityWeatherDTOBuilder cityWeatherDTO = CityWeatherDTO.builder();

        cityWeatherDTO.temperature( cityWeatherResponseCurrentWeatherTemperature( cityWeatherResponse ) );
        cityWeatherDTO.windSpeed( cityWeatherResponseCurrentWeatherWindSpeed( cityWeatherResponse ) );
        cityWeatherDTO.windDirection( cityWeatherResponseCurrentWeatherWindDirection( cityWeatherResponse ) );

        return cityWeatherDTO.build();
    }

    private Double cityWeatherResponseCurrentWeatherTemperature(CityWeatherResponse cityWeatherResponse) {
        CityWeatherResponse.CurrentWeather currentWeather = cityWeatherResponse.getCurrentWeather();
        if ( currentWeather == null ) {
            return null;
        }
        return currentWeather.getTemperature();
    }

    private Double cityWeatherResponseCurrentWeatherWindSpeed(CityWeatherResponse cityWeatherResponse) {
        CityWeatherResponse.CurrentWeather currentWeather = cityWeatherResponse.getCurrentWeather();
        if ( currentWeather == null ) {
            return null;
        }
        return currentWeather.getWindSpeed();
    }

    private Integer cityWeatherResponseCurrentWeatherWindDirection(CityWeatherResponse cityWeatherResponse) {
        CityWeatherResponse.CurrentWeather currentWeather = cityWeatherResponse.getCurrentWeather();
        if ( currentWeather == null ) {
            return null;
        }
        return currentWeather.getWindDirection();
    }
}
