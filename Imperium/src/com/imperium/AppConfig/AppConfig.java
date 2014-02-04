package com.imperium.AppConfig;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private Map<String, AppProperties> appProperties;

    public Map<String, AppProperties> getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(Map<String, AppProperties> appProperties) {
        this.appProperties = appProperties;
    }

    public AppConfig(){
        appProperties = new HashMap<String, AppProperties>();
    }
}
