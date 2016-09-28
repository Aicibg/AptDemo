package com.dhao.ioc_api;

import android.app.Activity;

/**
 * Created by DongHao on 2016/9/28.
 * Description:
 */

public class Ioc {
    public static void inject(Activity activity) {
        inject(activity, activity);
    }

    public static void inject(Object host, Object root) {
        Class<?> clazz = host.getClass();
        String proxyFullName = clazz.getName() + "$$ViewInject";
        try {
            Class<?> proxyClazz=Class.forName(proxyFullName);
            ViewInject viewInject= (ViewInject) proxyClazz.newInstance();
            viewInject.inject(host,root);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
