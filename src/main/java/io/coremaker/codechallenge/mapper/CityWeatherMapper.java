package io.coremaker.codechallenge.mapper;

import io.coremaker.codechallenge.dto.internal.CityWeatherDTO;
import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CityWeatherMapper {

    @Mapping(target = "temperature", source = "currentWeather.temperature")
    @Mapping(target = "windSpeed", source = "currentWeather.windSpeed")
    @Mapping(target = "windDirection", source = "currentWeather.windDirection")
    CityWeatherDTO toCityWeatherDTO(CityWeatherResponse cityWeatherResponse);
}
