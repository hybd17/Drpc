package org.example.core.config;

import lombok.Data;
import org.example.core.fault.retry.RetryStrategyKeys;
import org.example.core.fault.tolerant.TolerantStrategyKeys;
import org.example.core.loadbalancer.LoadBalancerKeys;
import org.example.core.serializer.SerializerKeys;

@Data
public class RpcConfig {
    private String name = "dy-rpc";
    private String version = "1.0";
    private String serverHost = "localhost";
    private Integer serverPort = 8080;
    private boolean mock = false;
    private String serializer = SerializerKeys.JDK;
    private RegistryConfig registryConfig = new RegistryConfig();
    private String loadBalancer = LoadBalancerKeys.ROUND_ROBIN;
    private String retryStrategy = RetryStrategyKeys.NO;
    private String tolerantStrategy = TolerantStrategyKeys.FAIL_FAST;
}
