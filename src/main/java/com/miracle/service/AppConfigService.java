package com.miracle.service;

import com.miracle.model.AppConfig;
import com.miracle.model.AppConfigDetail;
import com.miracle.repository.AppConfigRepository;
import com.miracle.repository.AppConfigDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppConfigService {

    private final AppConfigRepository appConfigRepository;
    private final AppConfigDetailRepository appConfigDetailRepository;

    public AppConfig getAppConfig(String appId) {
        return appConfigRepository.findByAppId(appId)
                .orElseThrow(() -> new RuntimeException("App config not found"));
    }

    public List<AppConfigDetail> getAppUpdates(String appId, Integer currentVersionCode) {
        return appConfigDetailRepository.findByAppConfig_AppIdAndVersionCodeGreaterThan(appId, currentVersionCode);
    }

    @Transactional
    public AppConfig createAppConfig(AppConfig appConfig) {
        return appConfigRepository.save(appConfig);
    }

    @Transactional
    public AppConfig updateAppConfig(String appId, AppConfig appConfig) {
        AppConfig existing = getAppConfig(appId);
        existing.setAppStatus(appConfig.getAppStatus());
        existing.setTitle(appConfig.getTitle());
        existing.setMessage(appConfig.getMessage());
        return appConfigRepository.save(existing);
    }

    @Transactional
    public AppConfigDetail addAppConfigDetail(AppConfigDetail detail) {
        return appConfigDetailRepository.save(detail);
    }
}