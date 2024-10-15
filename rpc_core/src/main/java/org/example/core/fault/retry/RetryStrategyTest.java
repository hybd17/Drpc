package org.example.core.fault.retry;

import org.example.core.model.RpcResponse;
import org.junit.Test;

public class RetryStrategyTest {
    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void testRetry() {
        try{
            RpcResponse rpcResponse = retryStrategy.doRetry(() -> {
                System.out.println("retrying...");
                throw new RuntimeException("retry failed");
            });
            System.out.println(rpcResponse);
        }catch (Exception e){
            System.out.println("多次重试失败");
            e.printStackTrace();
        }
    }
}
