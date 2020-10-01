package com.verifone.receipt.handler;

import com.verifone.receipt.dao.SMTPConfigDAO;
import com.verifone.svc.framework.endpoint.server.SvcEndpointProperties;
import com.verifone.svc.framework.endpoint.server.lb.ELBStatus;
import com.verifone.svc.framework.endpoint.server.lb.ILBStatusHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import static com.verifone.receipt.constant.ReceiptConstants.TWO_THOUSAND;

@Component
@Log4j2
public class SvcLBStatusHandler implements ILBStatusHandler {

    @Autowired
    private SMTPConfigDAO smtpConfigDAO;

    @Override
    public ELBStatus handle(HttpServletRequest request, HttpServletResponse response, SvcEndpointProperties endPoints) {
        return isSocketAlive() ? ELBStatus.OPERATIONAL : ELBStatus.DOWN;
    }

    private boolean isSocketAlive() {
        boolean isAlive = false;
        String host = null;
        try {
            host = smtpConfigDAO.getSMTPConfDetails().getHost();
            isAlive = checkSocketAlive(host, Integer.parseInt(smtpConfigDAO.getSMTPConfDetails().getPort()));
            log.info("SMTP host is Available :: Host = {}", host);
        } catch (Exception e) {
            log.error("SMTP socket is not Alive Host = {}, Reason : {}", host, e);
        }
        return isAlive;
    }

    public boolean checkSocketAlive(String hostName, int port) throws IOException {
        boolean isAlive = false;
        // Creates a socket address from a hostname and a port number
        SocketAddress socketAddress = new InetSocketAddress(hostName, port);
        Socket socket = new Socket();
        int timeout = TWO_THOUSAND;
        try {
            socket.connect(socketAddress, timeout);
            isAlive = true;

        } catch (SocketTimeoutException exception) {
            log.error("SocketTimeoutException Host : {}, Port : {}, Reason : {}", hostName, port, exception.getMessage());
        } catch (IOException exception) {
            log.error(
                    "IOException - Unable to connect to host : {}, Port : {}, Reason : {}", hostName , port, exception.getMessage());
        } finally {
            socket.close();
        }
        return isAlive;
    }
}