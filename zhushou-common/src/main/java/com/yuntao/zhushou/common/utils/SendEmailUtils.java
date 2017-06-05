package com.yuntao.zhushou.common.utils;

import org.apache.commons.collections4.MapUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/** 
 * Spring Mail 
 * API都在org.springframework.mail及其子包org.springframework.mail.javamail中封装， 
 * 且只提供了邮件发送的封装。 SimpleMailMessage: 对邮件的一个简单封装，只能用于表示一个纯文本的邮件，也不能包含附件等。 
 * JavaMailSenderImpl: 邮件发送器，主要提供了邮件发送接口、透明创建Java 
 * Mail的MimeMessage、及邮件发送的配置(如:host/port/username/password...)。 
 * MimeMailMessage、MimeMessageHelper：对MimeMessage进行了封装。 
 * Spring还提供了一个回调接口MimeMessagePreparator, 用于准备JavaMail的MIME信件 
 * 一下代码转载自:http://www.blogjava.net/tangzurui/archive/2008/12/08/244953.html 
 */
public class SendEmailUtils {

    private static String fromMail = "583697470@qq.com";

    private static Session session = initSession();


    public static void main(String[] args) throws Exception {  

//        initJavaMailSender();
//        JavaMailSender sender = initJavaMailSender();
         sendText("583697470@qq.com","test","111111");
        // sendHtml(sender);
        // sendTextWithImg(sender);  
//        sendWithAttament();
    }  
      

    private static Session initSession() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", "smtp.qq.com");
        properties.setProperty("mail.smtp.port", "587");
        properties.setProperty("mail.user", fromMail);
        properties.setProperty("mail.password", "ngeawyjsfoqhbeii");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = fromMail;
                String password = "ngeawyjsfoqhbeii";
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(properties, authenticator);
        return mailSession;

    }
      
    public static void sendText(String email,String subject,String content) {
        try {
            // 创建邮件消息
            MimeMessage message = new MimeMessage(session);
            // 设置发件人
            InternetAddress form =  new InternetAddress(fromMail);
            message.setFrom(form);

            // 设置收件人的邮箱
            InternetAddress to = new InternetAddress(email);
            message.setRecipient(Message.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject(subject);

            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 最后当然就是发送邮件啦
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
  
    public static void sendHtml(String email,String subject,String htmlContent) throws Exception {
        // 建立邮件消息,发送简单邮件和html邮件的区别  
        MimeMessage message = new MimeMessage(session);
        // MimeMessageHelper messageHelper = new  
        // MimeMessageHelper(mailMessage);这个构造函数会出现中文乱码的问题  
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "GBK");
  
        // 设置收件人，寄件人  
        messageHelper.setTo(fromMail);
        messageHelper.setFrom(email);
        messageHelper.setSubject(subject);

        // true 表示启动HTML格式的邮件  
        messageHelper.setText( htmlContent,  true);
  
        // 发送邮件  
        Transport.send(message);
  
    }
  

    public static void sendWithAttament(String email,String subject,String htmlContent,Map<String,String> fileDataMap) throws MessagingException {
        // 建立邮件消息,发送简单邮件和html邮件的区别  
        MimeMessage message = new MimeMessage(session);
        // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，  
        // multipart模式 为true时发送附件 可以设置html格式  
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "utf-8");
  
        // 设置收件人，寄件人  
        messageHelper.setTo(email);
        messageHelper.setFrom(fromMail);
        messageHelper.setSubject(subject);
        // true 表示启动HTML格式的邮件  
        messageHelper.setText(htmlContent, true);
  
        //
        if (MapUtils.isNotEmpty(fileDataMap)) {
            Set<Map.Entry<String, String>> entries = fileDataMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                InputStreamSource inputStreamSource = new ByteArrayResource(entry.getValue().getBytes());
                messageHelper.addAttachment(entry.getKey(), inputStreamSource);
            }
        }

        // 发送邮件  
        Transport.send(message);
  
    }  
}  