package org.example.core.fault.tolerant;

import org.example.core.model.RpcResponse;

import java.util.Map;

public class FailBackTolerantStrategy implements TolerantStrategy {
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        //TODO implement fail-back tolerant strategy
        // e.g.   get the mock
        return null;
    }
}
