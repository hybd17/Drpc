package org.example.core.registry;

import org.example.core.model.ServiceMetaInfo;

import java.util.List;

public class RegistryServiceCache {
    List<ServiceMetaInfo> serviceCache;

    void writeServiceCache(List<ServiceMetaInfo> newServiceCache) {
        this.serviceCache = newServiceCache;
    }

    List<ServiceMetaInfo> readServiceCache() {
        return this.serviceCache;
    }

    void clearServiceCache() {
        this.serviceCache = null;
    }
}
