package com.apiinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.apiinsight.entity.ApiLog;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long> {
}