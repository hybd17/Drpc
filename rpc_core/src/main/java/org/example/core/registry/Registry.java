package org.example.core.registry;

import org.example.core.config.RegistryConfig;
import org.example.core.model.ServiceMetaInfo;

import java.util.List;

public interface Registry {
    void init(RegistryConfig config);

    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    void heartBeat();

    void watch(String serviceNodeKey);

    void destroy();
}
