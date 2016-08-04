package com.yuntao.zhushou.common;


public interface ResourceFacade {

    /**
     * 系统自带的静态资源的key的前缀
     */
    String RES_PREFIX_SYS = "/_resource";

    String uploadFile(byte data[]);

    String uploadFile(byte data[], String fileName);
}
