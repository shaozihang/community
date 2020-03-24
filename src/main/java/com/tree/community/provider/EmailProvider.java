package com.tree.community.provider;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailProvider {

    @Value("${email.smtphost}")
    private String emailSMTPHost;

    @Value("${email.account}")
    private String emailAccount;

    @Value("${email.password}")
    private String emailPassword;


    public void sendEmail(String email, HttpServletRequest request) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", emailSMTPHost);// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");//设置是否使用ssl安全连接
        //得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        //设置发件人邮箱地址
        message.setFrom(new InternetAddress(emailAccount,"树洞社区", "UTF-8"));
        //设置收件人地址
        message.setRecipients(MimeMessage.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
        //设置邮件标题
        message.setSubject("【树洞社区】邮箱验证");
        //设置邮件内容
        int verifyCode = (int) ((Math.random() * 9 + 1) * 100000);
        message.setContent("<html><body><div>尊敬的用户,您好:</div>" +
                "<br>" +
                "<div>&nbsp;&nbsp;&nbsp;&nbsp;您正在树洞社区进行邮箱验证的操作，本次请求的邮件验证码是：<strong>"+ verifyCode + "</strong>(为了保证您账号的安全性，请您在5分钟内完成验证).</div>" +
                "<div>&nbsp;&nbsp;&nbsp;&nbsp;本验证码5分钟内有效，请及时输入。</div>" +
                "<br>" +
                "<div>&nbsp;&nbsp;&nbsp;&nbsp;为保证账号安全，请勿泄漏此验证码。</div>" +
                "<div>&nbsp;&nbsp;&nbsp;&nbsp;祝在【树洞社区】收获愉快！</div>" +
                "<br>" +
                "<div>&nbsp;&nbsp;&nbsp;&nbsp;（这是一封自动发送的邮件，请不要直接回复）</div></body></html>","text/html;charset=utf-8");
        //得到邮差对象
        Transport transport = session.getTransport();
        //连接自己的邮箱账户
        transport.connect(emailAccount, emailPassword);//密码为授权码
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        Date date = new Date();
        request.getSession().setAttribute("sendEmailTime",date.getTime());
        request.getSession().setAttribute("sendEmail",email);
        request.getSession().setAttribute("sendEmailCode",verifyCode);
    }
}
