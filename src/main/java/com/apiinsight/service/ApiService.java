package com.apiinsight.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.apiinsight.entity.ApiLog;
import com.apiinsight.repository.ApiLogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiService {

    @Autowired
    private ApiLogRepository repo;

    public ApiLog callApi(String url, String method) {

        long start = System.currentTimeMillis();

        RestTemplate restTemplate = new RestTemplate();
        ApiLog log = new ApiLog();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.valueOf(method.toUpperCase()),
                    null,
                    String.class
            );

            long end = System.currentTimeMillis();

            log.setUrl(url);
            log.setMethod(method);
            log.setStatus(response.getStatusCode().value());
            log.setResponseTime(end - start);
            log.setResponseBody(response.getBody());

        } catch (Exception e) {

            long end = System.currentTimeMillis();

            log.setUrl(url);
            log.setMethod(method);
            log.setStatus(500);
            log.setResponseTime(end - start);
            log.setResponseBody("ERROR: " + e.getMessage());
        }

        log.setTimestamp(LocalDateTime.now());

        return repo.save(log);
    }

    public List<ApiLog> getAll() {
        return repo.findAll();
    }
    public long getTotalRequests() {
        return repo.count();
    }

    public long getSuccessCount() {
        return repo.findAll().stream()
                .filter(log -> log.getStatus() == 200)
                .count();
    }

    public long getFailureCount() {
        return repo.findAll().stream()
                .filter(log -> log.getStatus() != 200)
                .count();
    }

    public double getAvgResponseTime() {
        return repo.findAll().stream()
                .mapToLong(ApiLog::getResponseTime)
                .average()
                .orElse(0);
    }
}