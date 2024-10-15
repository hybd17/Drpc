package org.example.core.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.core.serializer.Serializer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {
    //存储已加载的类
    public static Map<String, Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();

    //对象实例缓存
    public static Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    public static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    private static final String[] SCAN_PACKAGES = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};
    //动态加载的类列表
    public static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);


    //加载类型
    public static Map<String, Class<?>> load(Class<?> loadClass){
        log.info("load SPI: {}", loadClass.getName());
        Map<String, Class<?>> keyClassMap = new HashMap<>();
        for (String scanPackage : SCAN_PACKAGES) {
            List<URL> resources = ResourceUtil.getResources(scanPackage+loadClass.getName());
            for (URL resource : resources) {
                try{
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while ((line = bufferedReader.readLine())!= null) {
                        String[] split = line.split("=");
                        if (split.length > 1) {
                            String key = split[0];
                            String className = split[1];
                            keyClassMap.put(key, Class.forName(className));
                        }
                }
            }catch (Exception e){
                    log.error("load SPI error", e);
                }
            }
        }
        loaderMap.put(loadClass.getName(), keyClassMap);
        return keyClassMap;
    }

    //获取实例
    public static <T> T getInstance(Class<?> loadClass, String key){
        String loadClassName = loadClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(loadClassName);
        if (keyClassMap == null) {
            throw new RuntimeException(String.format("%s SPI not found",loadClassName));
        }
        if(!keyClassMap.containsKey(key)){
            throw new RuntimeException(String.format("%s SPI not found key: %s",loadClassName, key));
        }
        Class<?> clazz = keyClassMap.get(key);
        String implClassName = clazz.getName();
        if(!instanceMap.containsKey(implClassName)){
            try{
                instanceMap.put(implClassName, clazz.newInstance());
            }catch (InstantiationException | IllegalAccessException e){
                String errorMsg = String.format("%s SPI load instance error", implClassName);
                throw new RuntimeException(errorMsg, e);
            }
        }
        return (T) instanceMap.get(implClassName);
    }


    public static void loadAllSpi() {
        log.debug("load all SPI");
        for (Class<?> loadClass : LOAD_CLASS_LIST){
            load(loadClass);
        }
    }
}
