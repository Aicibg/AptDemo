package com.dhao.ioc_api;

/**
 * Created by DongHao on 2016/9/28.
 * Description:
 */

public interface ViewInject <T>{
    void inject(T t,Object object);
}
