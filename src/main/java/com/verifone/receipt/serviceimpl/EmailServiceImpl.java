/**********************************************************************
 * The EmailServiceImpl class is related to get the email              *
 * related Service operation.                                          *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                 *
 **********************************************************************/
package com.verifone.receipt.serviceimpl;

import com.verifone.cegp.security.StringUtil;
import com.verifone.receipt.exception.EmailServiceException;
import com.verifone.receipt.model.Mail;
import com.verifone.receipt.service.IEmailService;
import com.verifone.receipt.service.IHtmlFormattingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;

import static com.verifone.receipt.constant.ReceiptConstants.TEMPLATES;
import static com.verifone.receipt.constant.ReceiptConstants.UTF_CONTENT_TYPE;
import static javax.mail.Part.INLINE;

@Service
public class EmailServiceImpl implements IEmailService {

    private static final Logger LOG = LoggerFactory.getLogger(EmailServiceImpl.class);
    private static final String CONTENT_ID = "Content-ID";

    @Autowired
    private Session session;

    @Autowired
    public IHtmlFormattingService htmlFormattingService;

    @Value("${receipt.email.attachment}")
    private String emailAttachment;

    @Value("${receipt.preformatted.attachment}")
    private String preFormatAttachment;

    @Value("${receipt.file.location}")
    private String fileLocation;

    @Value("${receipt.mail.noReply}")
    private String noReply;

    public String sendEmailSimple(Mail mail) throws EmailServiceException {
        try {
            String content = mail.getContent();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(noReply, mail.getFrom()));
            message.setRecipients(Message.RecipientType.TO, mail.getTo());
            message.setSubject(mail.getSubject(), "UTF-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();

            DataSource source;

            mbp.setContent(content, UTF_CONTENT_TYPE);
            mp.addBodyPart(mbp);

            mbp = new MimeBodyPart();
            source = new FileDataSource(new File(fileLocation + TEMPLATES + mail.getImageName()));
            mbp.setDataHandler(new DataHandler(source));
            mbp.setDisposition(INLINE);
            mbp.addHeader(CONTENT_ID, "<sendEmailContent>");
            mp.addBodyPart(mbp);

            if ("yes".equalsIgnoreCase(preFormatAttachment)) {
                mbp = new MimeBodyPart();
                source = new FileDataSource(new File(fileLocation + TEMPLATES + "output.pdf"));
                mbp.setDataHandler(new DataHandler(source));
                mbp.setFileName("Receipt.pdf");
                mp.addBodyPart(mbp);
            }

            message.setContent(mp, UTF_CONTENT_TYPE);

            Transport.send(message);
            LOG.info("Email has been sent Successfully");
            return mail.getContent();
        } catch (Exception e) {
            throw new EmailServiceException("Error while sending email ", e);
        }
    }

    public String sendEmail(Mail mail) throws EmailServiceException {
        try {
            String content = mail.getContent();
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(noReply, mail.getFrom()));
            message.setRecipients(Message.RecipientType.TO, mail.getTo());
            message.setSubject(mail.getSubject(), "UTF-8");

            Multipart mp = new MimeMultipart();
            MimeBodyPart mbp = new MimeBodyPart();
            DataSource source;

            mbp.setContent(content, UTF_CONTENT_TYPE);
            mp.addBodyPart(mbp);

            if (!StringUtil.isNullOrEmpty(mail.getImageName())) {
                mbp = new MimeBodyPart();
                source = new FileDataSource(new File(fileLocation + TEMPLATES + mail.getImageName()));
                mbp.setDataHandler(new DataHandler(source));
                mbp.setFileName("Receipt.png");
                mp.addBodyPart(mbp);
            }

            message.setContent(mp, UTF_CONTENT_TYPE);

            Transport.send(message);
            LOG.info("Email has been sent Successfully");
            return mail.getContent();
        } catch (Exception e) {
            throw new EmailServiceException("Error while sending email ", e);
        }
    }
}