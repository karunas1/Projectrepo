package com.verifone.receipt.endpoint;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.verifone.api.receipt.model.ReceiptTransaction;
import com.verifone.api.receipt.model.SendReceipt;
import com.verifone.receipt.exception.*;
import com.verifone.receipt.model.Template;
import com.verifone.receipt.service.IReceiptService;
import com.verifone.receipt.util.MockData;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class ReceiptServiceEndpointTest {
    @Mock
    IReceiptService receiptService;

    @Mock
    HttpServletResponse response;
    @Mock
    MultipartFile file;
    @Mock
    HttpServletResponse httpServletResponse;
    @Mock
    Template template;

    @InjectMocks
    ReceiptServiceEndpoint receiptServiceEndpoint;

    ReceiptTransaction receiptTransaction = MockData.prepareReceiptTransactionObject();

    SendReceipt sendReceipt = MockData.prepareSendReceiptObject();

    String msg = "PCFET0NUWVBFIGh0bWw+CjxodG1sPgogICA8aGVhZD4KICAgICAgPHN0eWxlPgogICAgICAgICAqIHsKICAgICAgICAgZm9udDoyMHB4IGFyaWFsLCBzYW5zLXNlcmlmOwogICAgICAgICB9CiAgICAgICAgIGJvZHkgewoJCQl3aWR0aDogMTc5cHg7CiAgICAgICAgIH0KICAgICAgICAgdGFibGUgewogICAgICAgICB3aWR0aDogMTAwJTsKICAgICAgICAgYm9yZGVyOiBub25lOwogICAgICAgICBib3JkZXItc3BhY2luZzogMDsKICAgICAgICAgfQogICAgICAgICAuZW12X3RhYmxlIHsKICAgICAgICAgdGFibGUtbGF5b3V0OiBmaXhlZDsKICAgICAgICAgfQogICAgICAgICB0ZCwgdGggewogICAgICAgICBib3JkZXI6IDBweCBzb2xpZCAjZGRkZGRkOwogICAgICAgICB2ZXJ0aWNhbC1hbGlnbjogdGV4dC10b3A7CiAgICAgICAgIH0KICAgICAgICAgLmVtdl9rZXkgewogICAgICAgICB3b3JkLXdyYXA6IGJyZWFrLXdvcmQ7CiAgICAgICAgIHdpZHRoOiAzMCU7CiAgICAgICAgIHRleHQtYWxpZ246IGxlZnQ7CiAgICAgICAgIH0KICAgICAgICAgLmVtdl92YWx1ZSB7CiAgICAgICAgIHdvcmQtd3JhcDogYnJlYWstd29yZDsKICAgICAgICAgdGV4dC1hbGlnbjogcmlnaHQ7CiAgICAgICAgIH0KICAgICAgICAgLnN1YnRpdGxlIHsKICAgICAgICAgZm9udDogMjhweDsKICAgICAgICAgdGV4dC1hbGlnbjogY2VudGVyOwogICAgICAgICB9CiAgICAgICAgIC5sZWZ0IHsKICAgICAgICAgd2lkdGg6IDUwJQogICAgICAgICB0ZXh0LWFsaWduOiBsZWZ0OwogICAgICAgICB3aGl0ZS1zcGFjZTogbm93cmFwOwogICAgICAgICB9CiAgICAgICAgIC5yaWdodCB7CiAgICAgICAgIHdpZHRoOiA1MCU7CiAgICAgICAgIHRleHQtYWxpZ246IHJpZ2h0OwogICAgICAgICB3b3JkLWJyZWFrOiBicmVhay1hbGw7CiAgICAgICAgIH0KICAgICAgICAgLmxlZnROb1dpZHRoIHsKICAgICAgICAgdGV4dC1hbGlnbjogbGVmdDsKICAgICAgICAgd2hpdGUtc3BhY2U6IG5vd3JhcDsKICAgICAgICAgfQogICAgICAgICAucmlnaHROb1dpZHRoIHsKICAgICAgICAgdGV4dC1hbGlnbjogcmlnaHQ7CiAgICAgICAgIHdvcmQtYnJlYWs6IGJyZWFrLWFsbDsKICAgICAgICAgfQogICAgICAgICAuY2VudGVyIHsKICAgICAgICAgdGV4dC1hbGlnbjogY2VudGVyOwogICAgICAgICB9CiAgICAgIDwvc3R5bGU+CiAgIDwvaGVhZD4KICAgPGJvZHk+CiAgICAgIDwhLS0gQnVzaW5lc3MgaW5mb3JtYXRpb24gc3RhcnQgLS0+CiAgICAgIDxkaXYgaWQ9ImJ1c2luZXNzSW5mbyI+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIEJ1c2luZXNzIGluZm9ybWF0aW9uIGVuZCAtLT48IS0tIEJlZm9yZSBncmVldGluZyBzdGFydCAtLT48IS0tIEVtcHR5IC0tPjwhLS0gQmVmb3JlIGdyZWV0aW5nIGVuZCAtLT48IS0tIEdyZWV0aW5nIHN0YXJ0IC0tPgogICAgICA8ZGl2IGlkPSJjdXN0b21HcmVldGluZyI+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIEdyZWV0aW5nIGVuZCAtLT48IS0tIEFmdGVyIGdyZWV0aW5nIHN0YXJ0IC0tPjwhLS0gRW1wdHkgLS0+PCEtLSBBZnRlciBncmVldGluZyBlbmQgLS0+PCEtLSBDYXNoaWVyIG5hbWUgc3RhcnQgLS0+PCEtLSBFbXB0eSAtLT48IS0tIENhc2hpZXIgbmFtZSBlbmQgLS0+PCEtLSBCZWZvcmUgdHJhbnNhY3Rpb24gc3RhcnQgLS0+PCEtLSBFbXB0eSAtLT48IS0tIEJlZm9yZSB0cmFuc2FjdGlvbiBlbmQgLS0+PCEtLSBUcmFuc2FjdGlvbiBpbmZvcm1hdGlvbiBzdGFydCAtLT4KICAgICAgPGRpdiBpZD0idHJhbnNhY3Rpb25JbmZvcm1hdGlvbiI+CiAgICAgIDxkaXYgaWQ9IlRSQU5TQUNUSU9OX1NFQ1RJT04iPgogICAgICA8IS0tIFRJVExFIC0tPgogICAgICA8ZGl2IGlkPSJzdWJ0aXRsZSIgY2xhc3M9ImNlbnRlciI+CiAgICAgICAgIENhcmRob2xkZXIncyByZWNlaXB0CiAgICAgIDwvZGl2PgogICAgICA8IS0tICBUUkFOU0FDVElPTiBJTkZPIC0tPgogICAgICA8ZGl2IGlkPSJ0cmFuc2FjdGlvbl90eXBlIiBjbGFzcz0ic3VidGl0bGUiPgogICAgICAgICA8c3Ryb25nPgogICAgICAgICBQVVJDSEFTRQogICAgICAgICA8L3N0cm9uZz4KICAgICAgPC9kaXY+CiAgICAgIDxoci8+CiAgICAgIDxici8+CiAgICAgIDwhLS0gTUVSQ0hBTlQgQlVTSU5FU1MgSU5GTyAtLT4KICAgICAgPHRhYmxlIGlkPSJtZXJjaGFudF9idXNpbmVzc19pbmZvIj4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMiI+VmVyaWZvbmU8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjb2xzcGFuPSIyIj4zMDAgUyBXYWNrZXI8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD4KICAgICAgICAgICAgICAgNjA2MDYKICAgICAgICAgICAgICAgQ2hpY2FnbwogICAgICAgICAgICA8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjb2xzcGFuPSIyIj5JbGxpbm9pczwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNvbHNwYW49IjIiPlVuaXRlZCBTdGF0ZXM8L3RkPgogICAgICAgICA8L3RyPgogICAgICA8L3RhYmxlPgogICAgICA8YnIvPgogICAgICA8dGFibGU+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5QaG9uZTo8L3RkPgogICAgICAgICAgICA8dGQgY2xhc3M9InJpZ2h0Ij4xLTU1NS01NTUtNTU1NTwvdGQ+CiAgICAgICAgIDx0ci8+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5CdXMuUmVnLk5vOjwvdGQ+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0icmlnaHQiPjwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDxici8+CiAgICAgIDx0YWJsZT4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY2xhc3M9ImxlZnQiPjMxLzAzLzIwMjA8L3RkPgogICAgICAgICAgICA8dGQgY2xhc3M9InJpZ2h0Ij4xMjozNjwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDxici8+CiAgICAgIDwhLS0gQU1PVU5UIFRBQkxFIC0tPgogICAgICA8dGFibGUgaWQ9ImFtb3VudCI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5QVVJDSEFTRTwvdGQ+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0icmlnaHQiPkdCUCAxLjIzPC90ZD4KICAgICAgICAgPC90cj4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMiI+CiAgICAgICAgICAgICAgIDxoci8+CiAgICAgICAgICAgIDwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij48c3Ryb25nPlRPVEFMPC9zdHJvbmc+PC90ZD4KICAgICAgICAgICAgPHRkIGNsYXNzPSJyaWdodCI+PHN0cm9uZz5HQlAgMS4yMzwvc3Ryb25nPjwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDxici8+CiAgICAgIDwhLS0gQ1ZNIC0tPgogICAgICA8dGFibGUgaWQ9ImN2bSI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkPgogICAgICAgICAgICAgICBDVk06CiAgICAgICAgICAgICAgIE5vQ1ZNCiAgICAgICAgICAgIDwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDwhLS0gQ0FSRCBOQU1FIC0tPgogICAgICA8dGFibGUgaWQ9ImNhcmRfbmFtZSI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Ij5EZWJpdCBNYXN0ZXJDYXJkPC90ZD4KICAgICAgICAgICAgPHRkIGNsYXNzPSJyaWdodCI+UFNOOjAwPC90ZD4KICAgICAgICAgPHRyPgogICAgICA8L3RhYmxlPgogICAgICA8ZGl2IGlkPSJjYXJkX3RlY2giIGNsYXNzPSJsZWZ0Ij4KICAgICAgICAgQ09OVEFDVExFU1MgQ0hJUAogICAgICA8L2Rpdj4KICAgICAgPGRpdiBpZD0icGFuIiBjbGFzcz0ibGVmdCI+CiAgICAgICAgICoqKioqKioqKioqKjAwNTIKICAgICAgPC9kaXY+CiAgICAgIDx0YWJsZT4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY29sc3Bhbj0iMiI+VC1TL046NDAxLTg5Mi00OTM8L3RkPgogICAgICAgICA8L3RyPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD4KICAgICAgICAgICAgICAgVElEOjYxNDAwOTM0CiAgICAgICAgICAgIDwvdGQ+CiAgICAgICAgIDwvdHI+CiAgICAgIDwvdGFibGU+CiAgICAgIDwhLS0gQUNRVUlSRVIgTkFNRSAtLT4KICAgICAgPGRpdiBpZD0iYWNxdWlyZXJfbmFtZSI+CiAgICAgICAgIGFwaWd3CiAgICAgIDwvZGl2PgogICAgICA8ZGl2IGlkPSJtaWQiPgogICAgICAgICBNSUQ6MjYwNDU3CiAgICAgIDwvZGl2PgogICAgICA8dGFibGUgaWQ9ImF0Y19hZWQiPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0ibGVmdCI+QVRDOjBBQkE8L3RkPgogICAgICAgICAgICA8dGQgY2xhc3M9InJpZ2h0Ij5BRUQ6MTcwMjAxPC90ZD4KICAgICAgICAgPC90cj4KICAgICAgPC90YWJsZT4KICAgICAgPHRhYmxlIGlkPSJhaWQiPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZD5BSUQ6QTAwMDAwMDAwNDEwMTA8L3RkPgogICAgICAgICA8L3RyPgogICAgICA8L3RhYmxlPgogICAgICA8ZGl2IGlkPSJ0YyI+CiAgICAgICAgIFRDOkFENjVFMjgyOUFBMTFGMTcKICAgICAgPC9kaXY+CiAgICAgIDx0YWJsZSBpZD0iYXJjX2F1dGhfY29kZSI+CiAgICAgICAgIDx0cj4KICAgICAgICAgICAgPHRkIGNsYXNzPSJsZWZ0Tm9XaWR0aCI+QVJDOjwvdGQ+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0icmlnaHROb1dpZHRoIj5BVVRIIENPREU6MDA3OTAyPC90ZD4KICAgICAgICAgPC90cj4KICAgICAgPC90YWJsZT4KICAgICAgPHRhYmxlIGlkPSJyZWYiPgogICAgICAgICA8dHI+CiAgICAgICAgICAgIDx0ZCBjbGFzcz0ibGVmdCI+UkVGOjQ5PC90ZD4KICAgICAgICAgPC90cj4KICAgICAgPC90YWJsZT4KICAgICAgPHRhYmxlIGlkPSJ0dnJfdHNpIj4KICAgICAgICAgPHRyPgogICAgICAgICAgICA8dGQgY2xhc3M9ImxlZnQiPlRWUjowMDQwMDAwMDAxPC90ZD4KICAgICAgICAgICAgPHRkIGNsYXNzPSJyaWdodCI+VFNJOkU4MDA8L3RkPgogICAgICAgICA8L3RyPgogICAgICA8L3RhYmxlPgogICAgICA8YnIvPgogICAgICA8L3RhYmxlPgogICAgICA8IS0tIFRSQU5TQUNUSU9OIFJFU1VMVCAtLT4KICAgICAgPGRpdiBpZD0ibWVzc2FnZV9hcHByb3ZhbCIgY2xhc3M9InN1YnRpdGxlIj4KICAgICAgICAgPHN0cm9uZz4KICAgICAgICAgT05MSU5FIEFVVEhPUklaRUQKICAgICAgICAgMDAKICAgICAgICAgPC9zdHJvbmc+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIEFERElUSU9OQUwgTUVTU0FHRSAtLT4KICAgICAgPGRpdiBpZD0ibWVzc2FnZV9zdGF0dXMiIGNsYXNzPSJjZW50ZXIiPgogICAgICA8L2Rpdj4KICAgICAgPGJyLz4KICAgICAgPCEtLSBDVk0gU0lHTkFUVVJFIC0tPgogICAgICA8ZGl2IGlkPSJuZmkiIGNsYXNzPSJzdWJ0aXRsZSI+CiAgICAgICAgIDxkaXY+CiAgICAgICAgICAgIDxkaXYgaWQ9Im1lc3NhZ2VfcmV0YWluIiBjbGFzcz0iY2VudGVyIj5QTEVBU0UgUkVUQUlOIFJFQ0VJUFQ8L2Rpdj4KICAgICAgICAgPC9kaXY+CiAgICAgICAgIDwhLS0gRW1wdHkgLS0+CiAgICAgIDwvZGl2PgogICAgICA8IS0tIFRyYW5zYWN0aW9uIGluZm9ybWF0aW9uIGVuZCAtLT48IS0tIEFmdGVyIHRyYW5zYWN0aW9uIHN0YXJ0IC0tPjwhLS0gRW1wdHkgLS0+PCEtLSBBZnRlciB0cmFuc2FjdGlvbiBlbmQgLS0+PCEtLSBCZWZvcmUgY3VzdG9tIGZvb3RlciBzdGFydCAtLT48IS0tIEVtcHR5IC0tPjwhLS0gQmVmb3JlIGN1c3RvbSBmb290ZXIgZW5kIC0tPjwhLS0gQ3VzdG9tIGZvb3RlciBzdGFydCAtLT48IS0tIEVtcHR5IC0tPjwhLS0gQ3VzdG9tIGZvb3RlciBlbmQgLS0+PCEtLSBBZnRlciBjdXN0b20gZm9vdGVyIHN0YXJ0IC0tPjwhLS0gRW1wdHkgLS0+PCEtLSBBZnRlciBjdXN0b20gZm9vdGVyIGVuZCAtLT48IS0tIEZvb3RlciBzdGFydCAtLT4KICAgICAgPGRpdiBpZD0iZm9vdGVyIj4KICAgICAgICAgPGRpdiBjbGFzcz0iZm9vdGVyIj4gRm9yIG1vcmUgaW5mb3JtYXRpb24gcmVmZXIgdG8gdGhlIEV4Y2hhbmdlcyBhbmQgUmVmdW5kcyBpbiBvdXIgPGI+UHVyY2hhc2UgQ29uZGl0aW9ucy48L2I+IFRoaXMgcmVjZWlwdCBpcyBlc3NlbnRpYWwgZm9yIHJldHVybmluZyBvciBleGNoYW5naW5nIGl0ZW1zLiBZb3UgY2FuIHNob3cgaXQgb24gdGhlIHNjcmVlbiBvZiB5b3VyIG1vYmlsZSBkZXZpY2Ugb3IgcHJpbnQgaXQuIDwvZGl2PgogICAgICA8L2Rpdj4KICAgICAgPCEtLSBGb290ZXIgZW5kIC0tPgogICA8L2JvZHk+CjwvaHRtbD4=";

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendReceipt() {
        ResponseEntity result = receiptServiceEndpoint.sendReceipt(receiptTransaction);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendReceipt4Unprocess() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(InvalidTemplateException.class).when(receiptService).processReceipt(receiptTransaction);
        ResponseEntity result = receiptServiceEndpoint.sendReceipt(receiptTransaction);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendReceipt4FailedDependency() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(InvalidTemplateDataException.class).when(receiptService).processReceipt(receiptTransaction);
        ResponseEntity result = receiptServiceEndpoint.sendReceipt(receiptTransaction);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendReceipt4NotAcceptable() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(EmailServiceException.class).when(receiptService).processReceipt(receiptTransaction);
        ResponseEntity result = receiptServiceEndpoint.sendReceipt(receiptTransaction);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendReceipt4InternalError() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(NullPointerException.class).when(receiptService).processReceipt(receiptTransaction);
        ResponseEntity result = receiptServiceEndpoint.sendReceipt(receiptTransaction);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendHTMLEmail() {
        ResponseEntity result = receiptServiceEndpoint.sendHTMLEmail(sendReceipt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendHTMLEmail4Unprocess() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(InvalidTemplateDataException.class).when(receiptService).sendHTMLReceipt(sendReceipt);
        ResponseEntity result = receiptServiceEndpoint.sendHTMLEmail(sendReceipt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendHTMLEmail4NotAcceptable() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(EmailServiceException.class).when(receiptService).sendHTMLReceipt(sendReceipt);
        ResponseEntity result = receiptServiceEndpoint.sendHTMLEmail(sendReceipt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendHTMLEmail4Failed() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(InvalidTemplateException.class).when(receiptService).sendHTMLReceipt(sendReceipt);
        ResponseEntity result = receiptServiceEndpoint.sendHTMLEmail(sendReceipt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSendHTMLEmail4InternalErr() throws InvalidTemplateDataException, InvalidTemplateException, EmailServiceException {
        doThrow(NullPointerException.class).when(receiptService).sendHTMLReceipt(sendReceipt);
        ResponseEntity result = receiptServiceEndpoint.sendHTMLEmail(sendReceipt);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSaveTemplate() throws EmailServiceException {
        when(receiptService.saveTemplate(anyString())).thenReturn("saveTemplateResponse");
        ResponseEntity result = receiptServiceEndpoint.saveTemplate(msg);
        Assert.assertNotNull(result);
    }

    @Test
    public void testSaveTemplate4InternalErr() throws EmailServiceException {
        doThrow(NullPointerException.class).when(receiptService).saveTemplate(msg);
        ResponseEntity result = receiptServiceEndpoint.saveTemplate(msg);
        Assert.assertNotNull(result);
    }

    @Test
    public void testInvalidFormatException() {
        ResponseEntity<ReceiptServiceException> result = receiptServiceEndpoint.invalidFormatException(new InvalidFormatException("message", Object.class, ReceiptServiceEndpoint.class));
        Assert.assertNotNull(result);
    }

    @Test
    public void testHandleException() {
        ResponseEntity result = receiptServiceEndpoint.handleException();
        Assert.assertNotNull(result);
    }
    @Test
    public void testaddTemplate(){
        receiptServiceEndpoint.addTemplate(file);
    }

    @Test
    public void testaddTemplateException() throws IOException {
        doThrow(FileStorageException.class).when(receiptService).addTemplate(file);
        receiptServiceEndpoint.addTemplate(file);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testretriveTemplate() throws Exception {
        receiptServiceEndpoint.retrieveTemplate(Mockito.anyString(),response);
    }

    @Test
    public void testretriveTemplateException() throws Exception {
        doThrow(FileStorageException.class).when(receiptService).retrieveTemplate("filename");
        receiptServiceEndpoint.retrieveTemplate("filename",response);
    }

    @Test
    public void testupdateTemplate() throws Exception {
        receiptServiceEndpoint.updateTemplate(Mockito.any(MultipartFile.class),Mockito.anyString());

    }

    @Test
    public void testupdateTemplateException() throws Exception {
        when(receiptService.updateTemplate(Mockito.any(),Mockito.anyString())).thenThrow(FileStorageException.class);
        receiptServiceEndpoint.updateTemplate(Mockito.any(MultipartFile.class),Mockito.anyString());
    }

    @Test
    public void testdeleteTemplate() throws Exception {
        receiptServiceEndpoint.deleteTemplate("filename");
    }

    @Test
    public void testdeleteTemplateException() throws Exception {
        when(receiptService.deleteTemplate("filename")).thenThrow(FileStorageException.class);
        receiptServiceEndpoint.deleteTemplate("filename");
    }

    @Test
    public void testretrieveAllTemplate() throws IOException {
        receiptService.retrieveAllTemplate();
        receiptServiceEndpoint.retrieveAllTemplate();
    }

    @Test
    public void testretrieveAllTemplateException() throws IOException {
        Mockito.doThrow(FileStorageException.class).when(receiptService).retrieveAllTemplate();
        receiptServiceEndpoint.retrieveAllTemplate();
    }

    @Test
    public void testreprintReceipt(){
        receiptServiceEndpoint.reprintReceipt(receiptTransaction);
    }

    @Test
    public void testreprintReceiptException() throws Exception {
        Mockito.doThrow(ReceiptStorageException.class).when(receiptService).reprintReceipt(receiptTransaction);
        receiptServiceEndpoint.reprintReceipt(receiptTransaction);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testretrivereceipt() throws Exception {
        receiptServiceEndpoint.retrieveReceipt(Mockito.anyString(),Mockito.anyString(),httpServletResponse);
    }

    @Test
    public void testretrivereceiptException() throws Exception {
        doThrow(ReceiptStorageException.class).when(receiptService).retrieveReceipt(Mockito.anyString(),Mockito.anyString());
        receiptServiceEndpoint.retrieveReceipt(Mockito.anyString(),Mockito.anyString(),response);
    }

    @Test
    public void testgetListOfTemplates(){
        List<String> listOfTemplate= new ArrayList<>();
        when(receiptService.getListOfTemplates()).thenReturn(listOfTemplate);
        receiptServiceEndpoint.getListOfTemplates();
    }

    @Test
    public void testgetListOfTemplatesException(){
        Mockito.when(receiptService.getListOfTemplates()).thenThrow(FileStorageException.class);
        receiptServiceEndpoint.getListOfTemplates();
    }

}

