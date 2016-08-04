package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by shengshan.tang on 2015/6/18 0018 at 11:05
 */
public class CollectUtils {

    /**
     * list 转换
     * @param srcList
     * @param destClass
     * @param <T>
     * @return
     */
    public static<T> List<T> transList(List srcList,final  Class destClass){
        List newDataList = new ArrayList();
        CollectionUtils.collect(srcList, new Transformer() {
            @Override
            public Object transform(Object input) {
                return BeanUtils.beanCopy(input,destClass);
            }
        }, newDataList);
        return newDataList;
    }

    public static int size(Collection dataList){
        if(CollectionUtils.isNotEmpty(dataList)){
            return dataList.size();
        }
        return 0;
    }
    
   static class A{
    	public int a=1;
    	public A(){}
    	
    }
   static class B extends A {
    	
    	public int b;
    	
    	public B(){}
    }
    
    public  static void  main(String args[]){
    	 List<A> ac = new ArrayList<A>();
    	 A a=new A();
    	 ac.add(a);
    	 List<B> bc = new ArrayList<B>();
    	 bc = CollectUtils.transList(ac, B.class);
    	 System.out.println(bc.get(0).b);
    	
    }
    
}
