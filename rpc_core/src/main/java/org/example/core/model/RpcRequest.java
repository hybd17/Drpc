package org.example.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.constant.RpcConstant;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {
    //服务名称
    private String serviceName;
    //方法名称
    private String methodName;
    //参数类型 列表
    private Class<?>[] parameterTypes;
    //参数列表
    private Object[] args;
    //版本号
    private String serviceVersion = RpcConstant.DEFAULT_SERVICE_VERSION;
}
