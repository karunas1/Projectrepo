/**********************************************************************
 * The SMTPConfigDAOImpl class is related to get the SMTP			  *
 * configuration  from Database.                                       *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                 *
 **********************************************************************/
package com.verifone.receipt.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.verifone.receipt.dao.SMTPConfigDAO;
import com.verifone.receipt.exception.DaoException;
import com.verifone.receipt.model.SMTPConfig;

@Repository
public class SMTPConfigDAOImpl implements SMTPConfigDAO {

    private static final Logger LOG = LoggerFactory.getLogger(SMTPConfigDAOImpl.class);

    @Value("${receipt.mail.host}")
    private String host;

    @Value("${receipt.mail.port}")
    private int port;

    @Override
    public SMTPConfig getSMTPConfDetails() throws DaoException {
        LOG.info("Fetching SMTP config node");
        SMTPConfig smtpConfig = new SMTPConfig();
        try {
            readDefaultConfiguration(smtpConfig);
        } catch (Exception e) {
            LOG.info("Error getting SMTP Config");
        }
        return smtpConfig;
    }

    private void readDefaultConfiguration(SMTPConfig smtpConfig) {
        LOG.info("Reading the Default configuration");
        smtpConfig.setHost(host);
        smtpConfig.setPort(String.valueOf(port));
    }
}