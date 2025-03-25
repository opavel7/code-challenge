package io.coremaker.codechallenge.client;

import io.coremaker.codechallenge.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractApiClient {
    private final String CLASS_NAME = NominatimClient.class.getSimpleName();

    private final RestTemplate restTemplate;

    protected AbstractApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected abstract String getBaseUrl();

    protected <T> Optional<T> get(String path, Class<T> responseType, Map<String, String> headers, Object... uriVariables) {
        return executeRequest(path, responseType, headers, uriVariables);
    }

    protected <T> Optional<T> get(String path, ParameterizedTypeReference<T> responseType, Map<String, String> headers, Object... uriVariables) {
        return executeRequest(path, responseType, headers, uriVariables);
    }

    private <T> Optional<T> executeRequest(String path, Object responseType, Map<String, String> headers, Object... uriVariables) {
        String url = UriComponentsBuilder.fromUriString(getBaseUrl())
                .path(path)
                .buildAndExpand(uriVariables)
                .toUriString();

        var httpHeaders = new HttpHeaders();
        httpHeaders.setAll(headers);
        var requestEntity = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<T> responseEntity;

            if (responseType instanceof Class) {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, (Class<T>) responseType);
            } else {
                responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, (ParameterizedTypeReference<T>) responseType);
            }

            return Optional.ofNullable(responseEntity.getBody());

        } catch (HttpStatusCodeException e) {
            log.error("[{}] API call failed: {} - {}",CLASS_NAME, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalApiException("Failed API call: " + e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("[{}] Unexpected error while calling API: {}", CLASS_NAME, e.getMessage());
            throw new ExternalApiException("Unexpected API failure", e);
        }
    }
}
