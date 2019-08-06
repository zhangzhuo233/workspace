package com.bd.booksystem.util;

import com.bd.booksystem.constants.Constants;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Random;

/**
 * @program: bookSystem
 * @description: send message by email
 * @author: Mr.zhang
 * @create: 2019-08-06 16:57
 **/
public class SendMessage {
    public String sendToEmail(String email) {
        // 生成验证码
        Random random = new Random();
        int num = random.nextInt(100_000);
        String str = num + "";
        for (int i = str.length(); i < 6; i++) {
            str = "0" + str;
        }
        Properties pros = new Properties();
        try {
            InputStream is = new FileInputStream(Constants.EMAIL_PROPERTIES);
            pros.load(is);
            // 构建授权信息
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String user = pros.getProperty(Constants.EMAIL_USER);
                    String password = pros.getProperty(Constants.EMAIL_PASSWORD);
                    return new PasswordAuthentication(user, password);
                }
            };
            // 创建邮件会话
            Session mailSession = Session.getInstance(pros, authenticator);
            // 构建邮件信息MimeMessage对象
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            InternetAddress sender = new InternetAddress(pros.getProperty(Constants.EMAIL_USER));
            message.setFrom(sender);
            // 设置收件人
            InternetAddress reciever = new InternetAddress(email);
            message.setRecipient(MimeMessage.RecipientType.TO, reciever);
            // 设置邮件邮件信息
            message.setSubject("邮件验证");
            String msg = "您的验证码是："+ str + "请不要泄露给他人";
            // 打印验证码
            // System.out.println(msg);
            message.setContent(msg, "text/html;charset=utf-8");
            // System.out.println("-----");
            // 发送
            Transport.send(message);
            // System.out.println("发送成功!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
