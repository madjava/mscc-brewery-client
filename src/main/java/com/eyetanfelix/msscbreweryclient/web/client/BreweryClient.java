package com.eyetanfelix.msscbreweryclient.web.client;

import com.eyetanfelix.msscbreweryclient.web.model.BeerDto;
import com.eyetanfelix.msscbreweryclient.web.model.CustomerDto;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.UUID;

@Component
@ConfigurationProperties(value = "sfg.brewery", ignoreUnknownFields = false)
public class BreweryClient {

    public final String BEER_PATH_V1 = "/api/v1/beer/";
    public final String CUSTOMER_PATH_V1 = "/api/v1/customer/";

    private String apiHost;
    private final RestTemplate restTemplate;

    public BreweryClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public BeerDto getBeerById(UUID uuid) {
        return restTemplate.getForObject(apiHost + BEER_PATH_V1 + uuid.toString(), BeerDto.class);
    }

    public URI saveNewBear(BeerDto beerDto) {
        return restTemplate.postForLocation(apiHost + BEER_PATH_V1, beerDto);
    }

    public void updateBeer(UUID uuid, BeerDto beerDto) {
        restTemplate.put(apiHost + BEER_PATH_V1 + uuid.toString(), beerDto);
    }

    public void deleteBeer(UUID uuid) {
        restTemplate.delete(apiHost + BEER_PATH_V1 + uuid);
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public CustomerDto getCustomerById(UUID customerId) {
        return restTemplate.getForObject(apiHost + CUSTOMER_PATH_V1 + customerId, CustomerDto.class);
    }

    public URI saveNewCustomer(CustomerDto dto) {
        return restTemplate.postForLocation(apiHost + CUSTOMER_PATH_V1, dto);
    }

    public void updateCustomer(CustomerDto customerDto) {
        restTemplate.put(apiHost + CUSTOMER_PATH_V1 + customerDto.getId().toString(), customerDto);
    }

    public void deleteCustomer(UUID customerId) {
        restTemplate.delete(apiHost + CUSTOMER_PATH_V1 + customerId);
    }
}
