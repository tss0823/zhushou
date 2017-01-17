package com.yuntao.zhushou.common.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yuntao.zhushou.common.web.DocReqFieldObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

/**
 * 利用　kryo　java 序列化
 * Created by shan on 2017/1/17.
 */
public class SerializeNewUtil {

    private static final Log log = LogFactory.getLog(SerializeNewUtil.class);

    private static Kryo kryo = new Kryo();

    /**
     * 序列化
     *
     * @param object
     * @return
     */
    public static byte[] serialize(Object object) {
        Output output = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            output = new Output(baos);
            kryo.writeClassAndObject(output, object);
            output.close();
            byte[] bytes = baos.toByteArray();
            return bytes;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (output != null) {
                output.close();
            }
        }
        return null;
    }

    /**
     * 反序列化
     *
     * @param bytes
     * @return
     */
    public static Object unserialize(byte[] bytes) {
        Input input = null;
        try {
            ByteArrayInputStream bios = new ByteArrayInputStream(bytes);
            input = new Input(bios);
            return kryo.readClassAndObject(input);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (input != null) {
                input.close();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        DocReqFieldObject docReqFieldObject = new DocReqFieldObject();
        docReqFieldObject.setCode("abc");
        docReqFieldObject.setComment("xxxxxx");
        byte[] bytes = serialize(docReqFieldObject);

        Object resultObj = unserialize(bytes);
        System.out.printf("resultObj=" + resultObj);


    }
}
