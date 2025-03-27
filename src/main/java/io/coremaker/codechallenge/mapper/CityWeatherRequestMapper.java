package io.coremaker.codechallenge.mapper;

import io.coremaker.codechallenge.dto.external.nominatim.CityCoordinatesResponse;
import io.coremaker.codechallenge.dto.external.openmeteo.CityWeatherRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityWeatherRequestMapper {
    CityWeatherRequest toCityWeatherDTO(CityCoordinatesResponse cityCoordinatesResponse);
}
