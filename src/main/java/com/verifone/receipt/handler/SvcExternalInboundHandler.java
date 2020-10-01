package com.verifone.receipt.handler;

import com.verifone.svc.framework.endpoint.server.external.IExternalInboundHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class SvcExternalInboundHandler implements IExternalInboundHandler {

    @Override
    public HttpStatus handle(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        return null;
    }

    @Override
    public Class<?> requestType() {
        return null;
    }
}