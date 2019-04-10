package com.cxz.icolibrary;

import android.app.Activity;
import android.view.View;

import com.cxz.icolibrary.annotation.ContentView;
import com.cxz.icolibrary.annotation.EventBase;
import com.cxz.icolibrary.annotation.InjectView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class InjectManager {

    public static void inject(Activity activity) {
        // 布局的注入
        injectLayout(activity);

        // 控件的注入
        injectViews(activity);

        // 事件的注入
        injectEvents(activity);
    }

    private static void injectLayout(Activity activity) {
        // 获取类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取这个类上的注解
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if (contentView != null) {
            // 获取这个注解的值
            int layoutId = contentView.value();
            // 执行方法：setContentView(R.layout.activity_main);

            // 第一种方法
            // activity.setContentView(layoutId);

            // 第二种方法：通过反射执行方法
            try {
                Method method = clazz.getMethod("setContentView", int.class);
                // 执行方法
                method.invoke(activity, layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectViews(Activity activity) {
        // 获取类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取类的所有属性
        Field[] fields = clazz.getDeclaredFields();
        // 循环，拿到每个属性
        for (Field field : fields) {
            // 获取每个属性上的注解
            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.value();
                try {
                    // 执行方法：findViewById(R.id.xx);
                    Method method = clazz.getMethod("findViewById", int.class);
                    Object view = method.invoke(activity, viewId);
                    field.setAccessible(true); // 设置private属性可访问
                    // 将执行的方法的返回值赋值给属性
                    field.set(activity, view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectEvents(Activity activity) {
        // 获取类
        Class<? extends Activity> clazz = activity.getClass();
        // 获取当前类所有的方法
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            // 获取每个方法的注解
            Annotation[] annotations = method.getAnnotations();
            // 遍历方法的多个注解
            for (Annotation annotation : annotations) {
                // 获取注解上的注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType != null) {
                    // 通过EventBase获取3个规律
                    EventBase eventBase = annotationType.getAnnotation(EventBase.class);
                    if (eventBase != null) {
                        String listenerSetter = eventBase.listenerSetter();
                        Class<?> listenerType = eventBase.listenerType();
                        String callBackListener = eventBase.callBackListener();

                        // 注解的值
                        try {
                            // 通过 annotationType 获取OnClick注解的value的值
                            Method valueMethod = annotationType.getDeclaredMethod("value");
                            // 执行value方法获得注解的值
                            int[] viewIds = (int[]) valueMethod.invoke(annotation);

                            ListenerInvocationHandler handler = new ListenerInvocationHandler(activity);
                            handler.addMethodMap(callBackListener, method);

                            // 打包之后，用代理处理后续工作
                            Object listener = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                    new Class[]{listenerType}, handler);

                            for (int viewId : viewIds) {
                                // 控件的赋值过程
                                View view = activity.findViewById(viewId);
                                // 获取方法
                                Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                                // 执行方法
                                setter.invoke(view, listener);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        }
    }

}
