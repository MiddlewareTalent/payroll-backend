package com.payroll.uk.payroll_processing.fps.response;

import uk.gov.govtalk.cm.envelope.GovTalkMessage;

import jakarta.xml.bind.JAXBContext;
import java.io.StringReader;

public class GovTalkResponseParser {

    public static GovTalkResponseDTO parse(String responseXml) throws Exception {
        GovTalkResponseDTO dto = new GovTalkResponseDTO();

        // Unmarshal XML → GovTalkMessage object
        JAXBContext ctx = JAXBContext.newInstance(GovTalkMessage.class);
        GovTalkMessage govTalkMessage = (GovTalkMessage) ctx.createUnmarshaller()
                .unmarshal(new StringReader(responseXml));

        // Extract correlation ID
        if (govTalkMessage.getHeader() != null &&
                govTalkMessage.getHeader().getMessageDetails() != null) {
            dto.setCorrelationId(govTalkMessage.getHeader().getMessageDetails().getCorrelationID());
        }

        // Extract transaction ID (if present in body details)
        if (govTalkMessage.getBody() != null &&
                govTalkMessage.getBody().getAny() != null &&
                !govTalkMessage.getBody().getAny().isEmpty()) {
            // Sometimes TransactionID is in the first child
            Object first = govTalkMessage.getBody().getAny().get(0);
            if (first != null) {
                dto.setTransactionId(first.toString());
            }
        }

        // Check for errors
        if (govTalkMessage.getGovTalkDetails() != null &&
                govTalkMessage.getGovTalkDetails().getGovTalkErrors() != null &&
                !govTalkMessage.getGovTalkDetails().getGovTalkErrors().getError().isEmpty()) {

            govTalkMessage.getGovTalkDetails().getGovTalkErrors().getError()
                    .forEach(error -> {
                        if (error.getText() != null) {
                            error.getText().forEach(txt -> dto.getErrorMessages().add(txt));
                        }
                    });
            dto.setStatus("Rejected");

        } else {
            dto.setStatus("Accepted"); // Simplified assumption
        }

        return dto;
    }
}

