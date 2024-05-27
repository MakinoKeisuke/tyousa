package com.example.nagoyameshi.event;

import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.context.event.EventListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.Member;
import com.example.nagoyameshi.service.VerificationTokenService;

@Component			 
public class SignupEventListener {
	private static final Logger logger = Logger.getLogger(SignupEventListener.class.getName());
	private final VerificationTokenService verificationTokenService;
	private final JavaMailSender javaMailSender;
	
	public SignupEventListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
		this.verificationTokenService = verificationTokenService;
		this.javaMailSender = mailSender;
	}
	
	@EventListener
	private void onSignupEvent(SignupEvent signupEvent) {
		Member member = signupEvent.getMember();
		String token = UUID.randomUUID().toString();
		verificationTokenService.create(member, token);
		
		String recipientAddress = member.getEmail();
		String subject = "メール認証";
		String confirmationUrl = signupEvent.getRequestUrl() + "/verify?token=" + token;
		String message = "以下のリンクをクリックして会員登録を完了してください。";
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setFrom("makino.19820629@gmail.com");
		mailMessage.setTo(recipientAddress);
		mailMessage.setSubject(subject);
		mailMessage.setText(message + "\n" + confirmationUrl);
		javaMailSender.send(mailMessage);
		
		try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            logger.severe("Error sending email: " + e.getMessage());
            e.printStackTrace();
        }
	}
}
