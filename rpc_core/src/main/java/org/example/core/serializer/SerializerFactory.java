package org.example.core.serializer;

import org.example.core.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;


public class SerializerFactory {
//    private static final Map<String, Serializer> KEY_SERIALIZER_MAP = new HashMap<>(){
//        {
//            put(SerializerKeys.JSON, new JsonSerializer());
//            put(SerializerKeys.JDK,new JdkSerializer());
//            put(SerializerKeys.KRYO, new KryoSerializer());
//            put(SerializerKeys.HESSIAN, new HessianSerializer());
//        }
//    };
    static {
        SpiLoader.load(Serializer.class);
}

    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

    private static volatile Map<String, Serializer> serializerMap;

    //双检索单例模式
    public static Serializer getInstance(String key) {
        if(serializerMap==null){
            synchronized (SerializerFactory.class){
                if(serializerMap==null){
                    SpiLoader.load(Serializer.class);
                    serializerMap = new HashMap<>();
                }
            }
        }
        return SpiLoader.getInstance(Serializer.class,key);
    }
}
