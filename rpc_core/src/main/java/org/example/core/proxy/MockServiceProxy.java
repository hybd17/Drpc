package org.example.core.proxy;

import lombok.extern.slf4j.Slf4j;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Slf4j
public class MockServiceProxy implements InvocationHandler {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock invoke {}",method.getName());
        return getDefaultValue(methodReturnType);
    }

    /**
     * 生成指定类型的默认值对象
     * */
    private Object getDefaultValue(Class<?> returnType) {
        if (returnType.isPrimitive()){
            if (returnType == int.class) {
                return 0;
            } else if (returnType == boolean.class) {
                return false;
            } else if (returnType == long.class) {
                return 0L;
            } else if (returnType == double.class) {
                return 0.0;
            } else if (returnType == float.class) {
                return 0.0f;
            } else if (returnType == short.class) {
                return (short) 0;
            } else if (returnType == byte.class) {
                return (byte) 0;
            } else if (returnType == char.class) {
                return '\u0000';
            }
        }
        return null;
    }
}
