package org.example.core.fault.tolerant;

import org.example.core.model.RpcResponse;

import java.util.Map;

public class FailOverTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        // TODO: implement failover strategy
        return null;
    }
}
