package org.example.core.loadbalancer;

import org.example.core.model.ServiceMetaInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoadBalancerTest {
    final LoadBalancer loadBalancer = new ConsistentHashLoadBalancer();

    @Test
    public void test() {
        Map<String,Object> requestParams = new HashMap<>();
        requestParams.put("aaamethodName","apple---------");
        Map<String,Object> requestParams1 = new HashMap<>();
        requestParams.put("bbbmethodName111","dy---------");
        Map<String,Object> requestParams2 = new HashMap<>();
        requestParams.put("cccmethodName222","thy---------");
        ServiceMetaInfo serviceMetaInfo1 = new ServiceMetaInfo();
        serviceMetaInfo1.setServiceName("111myService");
        serviceMetaInfo1.setServiceVersion("1.0");
        serviceMetaInfo1.setServiceHost("localhost");
        serviceMetaInfo1.setServicePort(1234);
        ServiceMetaInfo serviceMetaInfo2 = new ServiceMetaInfo();
        serviceMetaInfo2.setServiceName("222myService");
        serviceMetaInfo2.setServiceVersion("1.0");
        serviceMetaInfo2.setServiceHost("dy.icu");
        serviceMetaInfo2.setServicePort(80);
        List<ServiceMetaInfo> serviceMetaInfoList = Arrays.asList(serviceMetaInfo1, serviceMetaInfo2);


        System.out.println((int)requestParams.hashCode());
        System.out.println((int)requestParams1.hashCode());
        System.out.println((int)requestParams2.hashCode());
        System.out.println((int)serviceMetaInfo1.getServiceName().hashCode());
        System.out.println((int)serviceMetaInfo2.getServiceName().hashCode());

        ServiceMetaInfo serviceMetaInfo = loadBalancer.select(requestParams, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(requestParams1, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
        serviceMetaInfo = loadBalancer.select(requestParams2, serviceMetaInfoList);
        System.out.println(serviceMetaInfo);
        Assert.assertNotNull(serviceMetaInfo);
    }
}
