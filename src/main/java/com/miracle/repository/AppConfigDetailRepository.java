package com.miracle.repository;

import com.miracle.model.AppConfigDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AppConfigDetailRepository extends JpaRepository<AppConfigDetail, Long> {
    List<AppConfigDetail> findByAppConfig_AppId(String appId);
    List<AppConfigDetail> findByAppConfig_AppIdAndVersionCodeGreaterThan(String appId, Integer versionCode);
}