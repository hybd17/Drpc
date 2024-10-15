package org.example.core.loadbalancer;

import org.example.core.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

public interface LoadBalancer {

    /**
     * @param requestParams 请求参数
     * @param serviceMetaInfoList 服务元信息列表
     * @return 选择的服务元信息
     * */
    ServiceMetaInfo select(Map<String,Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList);
}
