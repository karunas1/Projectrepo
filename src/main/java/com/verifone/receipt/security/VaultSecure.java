package com.verifone.receipt.security;

import com.verifone.cegp.security.vault.VaultConstant;
import com.verifone.cegp.security.vault.VaultCtrl;
import com.verifone.receipt.exception.ReceiptServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class VaultSecure {

    private static final Logger LOGGER = LoggerFactory.getLogger(VaultSecure.class);

    @Value("${vault.alias}")
    private String vaultKeystoreAlias;

    @Value("${vault.create}")
    private String vaultKeystoreCreate;

    @Value("${vault.enc.file.dir}")
    private String vaultKeystoreDir;

    @Value("${vault.iteration.count}")
    private String vaultKeystoreIterationCount;

    @Value("${vault.password}")
    private String vaultKeystorePassword;

    @Value("${vault.url}")
    private String vaultKeystoreUrl;

    private Map<String, Object> options = new HashMap<>();

    public void initVault() throws ReceiptServiceException {
        options.put(VaultConstant.KEYSTORE_ALIAS, vaultKeystoreAlias);
        options.put(VaultConstant.KEYSTORE_CREATE_KEYSTORE, vaultKeystoreCreate);
        options.put(VaultConstant.KEYSTORE_ENC_FILE_DIR, vaultKeystoreDir);
        options.put(VaultConstant.KEYSTORE_ITERATION_COUNT, vaultKeystoreIterationCount);
        options.put(VaultConstant.KEYSTORE_PASSWORD, vaultKeystorePassword);
        options.put(VaultConstant.KEYSTORE_URL, vaultKeystoreUrl);
        try {
            VaultCtrl.init(options);
            LOGGER.info("Vault Initialized successfully.");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ReceiptServiceException("Error while initializing vault.");
        }
    }

    public String retrievePassword(String key) throws ReceiptServiceException {
        String pwd = null;
        try {
            pwd = VaultCtrl.retrieve(key);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            throw new ReceiptServiceException("Error while retrieving Password.");
        }
        return pwd;
    }

    @PostConstruct
    void init() throws ReceiptServiceException {
        try {
            initVault();
        } catch (Exception e) {
            LOGGER.error("Exception occurred while initializing vault.", e);
            throw new ReceiptServiceException("Error while initializing vault.");
        }
    }

}
