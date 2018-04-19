/**
 * 
 */
package com.yuntao.zhushou.common.utils;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.slf4j.Logger;

import java.io.StringWriter;
import java.util.Map;

/**
 * velocity 模板工具类
 * @author tangss
 *
 * @2013年8月31日 @下午4:30:32
 */
public class VelocityRenderUtils {
	static String  LOGGER_NAME = "velexample";
	private  static Logger log = org.slf4j.LoggerFactory.getLogger(LOGGER_NAME);
	
	static{
		
		
	}
	
	/**
	 * 合并模板并获得内容
	 * @param paramMap
	 * @param templateDir
	 * @param templateFileName
	 * @return
	 */
	public static String getContent(Map<String,Object> paramMap,String templateDir,String templateFileName){
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty( RuntimeConstants.RUNTIME_LOG_LOGSYSTEM_CLASS,
				"org.apache.velocity.runtime.log.Log4JLogChute" );
		engine.setProperty("runtime.log.logsystem.log4j.logger",
				LOGGER_NAME);
		engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_CACHE, false);
		engine.setProperty(RuntimeConstants.INPUT_ENCODING, "utf-8");
		engine.setProperty(RuntimeConstants.OUTPUT_ENCODING, "utf-8");
		
		log.info("templateDir="+templateDir);
		log.info("templateFileName="+templateFileName);
		engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH,templateDir);
		engine.init();
		VelocityContext context = new VelocityContext(paramMap);
		Template template = null;
		try{
		   template = engine.getTemplate(templateFileName);
		}
		catch( Exception e ){
		  log.error("getTemplate error! ",e);
		  throw new RuntimeException(e);
		}
		StringWriter sw = new StringWriter();
		template.merge( context, sw );
		return sw.toString();
	}

	public static void main(String[] args) {

	}
}
