/**********************************************************************
 * The SMTPConfig class is related to handle  the model data 	      *
 * from the Database.                                                  *
 *                                                                     *
 * @version 1.0                                                        *
 * @since 06/04/2019                                                 *
 **********************************************************************/
package com.verifone.receipt.model;

import java.io.Serializable;

public class SMTPConfig implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;

    private String host;

    private String port;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

}