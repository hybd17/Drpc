package org.example.core.fault.tolerant;

import lombok.extern.slf4j.Slf4j;
import org.example.core.model.RpcResponse;

import java.util.Map;

@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) {
        log.info("FailSafe to handle the exception",e);
        return new RpcResponse();
    }
}
