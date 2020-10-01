package com.verifone.receipt.handler;

import com.verifone.svc.framework.endpoint.server.transaction.ITransactionHandler;
import com.verifone.svc.framework.endpoint.server.transaction.contracts.SvcRestRequest;
import com.verifone.svc.framework.endpoint.server.transaction.contracts.SvcRestResponse;
import org.springframework.stereotype.Component;

@Component
public class SvcTransactionHandler implements ITransactionHandler {

    @Override
    public SvcRestResponse post(SvcRestRequest request) {
        return new SvcRestResponse();
    }

    @Override
    public SvcRestResponse put(SvcRestRequest request) {
        return new SvcRestResponse();
    }

    @Override
    public SvcRestResponse patch(SvcRestRequest request) {
        return new SvcRestResponse();
    }

    @Override
    public SvcRestResponse get(SvcRestRequest request) {
        return new SvcRestResponse();
    }

    @Override
    public SvcRestResponse delete(SvcRestRequest request) {
        return new SvcRestResponse();
    }
}