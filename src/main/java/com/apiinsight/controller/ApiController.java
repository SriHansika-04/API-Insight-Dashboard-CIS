package com.apiinsight.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.apiinsight.service.ApiService;

import java.util.*;

@RestController
@CrossOrigin
public class ApiController {

    @Autowired
    private ApiService service;

    @PostMapping("/test-api")
    public Object testApi(@RequestBody Map<String, String> data) {
        return service.callApi(data.get("url"), data.get("method"));
    }

    @GetMapping("/history")
    public Object history() {
        return service.getAll();
    }

    // ✅ THIS IS WHAT YOU ADDED
    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        Map<String, Object> stats = new HashMap<>();

        stats.put("totalRequests", service.getTotalRequests());
        stats.put("successCount", service.getSuccessCount());
        stats.put("failureCount", service.getFailureCount());
        stats.put("avgResponseTime", service.getAvgResponseTime());

        return stats;
    }
}