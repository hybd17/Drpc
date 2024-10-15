package org.example.core.loadbalancer;

import org.example.core.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ConsistentHashLoadBalancer implements LoadBalancer{

    private final TreeMap<Integer, ServiceMetaInfo> virtualNodes = new TreeMap<>();
    private static final int VIRTUAL_NODE_NUM = 100;

    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParams, List<ServiceMetaInfo> serviceMetaInfoList) {
        if(serviceMetaInfoList.isEmpty())
            return null;
        //develop virtual nodes for each service
        for(ServiceMetaInfo serviceMetaInfo:serviceMetaInfoList){
            for(int i = 0;i<VIRTUAL_NODE_NUM;i++){
                int hash = getHash(serviceMetaInfo.getServiceName() + "#" + i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }

        int hash = getHash(requestParams);

        //TreeMap.cellEntry 返回与大于或等于给定键元素 (ele) 的最小键元素链接的键值对
        //在此处检测哈希环上最近的点
        Map.Entry<Integer, ServiceMetaInfo> entry = virtualNodes.ceilingEntry(hash);
        if(entry==null){
            entry = virtualNodes.firstEntry();
        }
        return entry.getValue();
 }

    private int getHash(Object key){
        return key.hashCode();
        //TODO string.hashCode() is not a good choice, we should use a more complex hash function
//        new String().hashCode()
    }
}
