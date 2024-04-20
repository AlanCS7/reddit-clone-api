package io.github.alancs7.redditclone.service;

import io.github.alancs7.redditclone.exception.EmailException;
import io.github.alancs7.redditclone.model.NotificationEmail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
            messageHelper.setFrom("redditclone@email.com");
            messageHelper.setTo(notificationEmail.recipient());
            messageHelper.setSubject(notificationEmail.subject());
            messageHelper.setText(mailContentBuilder.build(notificationEmail.body()), true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Activation e-mail sent.");
        } catch (MailException e) {
            throw new EmailException("Exception occurred when sending mail to " + notificationEmail.recipient(), e);
        }
    }
}
