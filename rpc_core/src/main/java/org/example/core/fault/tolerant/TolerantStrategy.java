package org.example.core.fault.tolerant;

import org.example.core.model.RpcResponse;

import java.util.Map;

public interface TolerantStrategy {

    /**
     * @param context 上下文 传递数据
     * @param e 异常
     * */

    RpcResponse doTolerant(Map<String,Object> context,Exception e);
}
