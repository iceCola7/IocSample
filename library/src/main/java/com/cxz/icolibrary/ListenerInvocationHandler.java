package com.cxz.icolibrary;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ListenerInvocationHandler implements InvocationHandler {

    // 需要拦截的对象
    private Object target;
    // 需要拦截的方法集合
    private HashMap<String, Method> methodMap = new HashMap<>();

    public ListenerInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (target != null) {
            // 获取需要拦截的方法名
            String methodName = method.getName();
            // 重新赋值，将拦截的方法替换为了自定义的方法
            method = methodMap.get(methodName); // 从集合中获取是否需要拦截
            if (method != null) {
                // 确实找不到了需要拦截的方法，才执行自定义的方法
                if (method.getGenericParameterTypes().length == 0)
                    return method.invoke(target);
                return method.invoke(target, args);
            }
        }
        return null;
    }

    /**
     * 将需要拦截的方法加入到集合
     *
     * @param methodName 需要拦截的方法，比如：onClick()
     * @param method     执行自定义的方法，比如：doClick()
     */
    public void addMethodMap(String methodName, Method method) {
        methodMap.put(methodName, method);
    }

}
