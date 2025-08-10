package com.jc.template.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
@Slf4j
public abstract class BaseController {

    protected final RestTemplate restTemplate;

    @Value("${template.api.url}")
    protected String templateApiBaseUrl;

    protected static final ObjectMapper objectMapper = new ObjectMapper();

    public BaseController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}