package com.payroll.uk.payroll_processing.fps.service;

import com.payroll.uk.payroll_processing.fps.builder.FpsXmlBuilder;
import com.payroll.uk.payroll_processing.fps.builder.envelope.GovTalkHeaderInfo;
import com.payroll.uk.payroll_processing.fps.builder.envelope.GovTalkMessageBuilder;
import com.payroll.uk.payroll_processing.fps.dto.FpsSubmissionRequest;
import com.payroll.uk.payroll_processing.fps.entity.FpsSubmissionLog;
import com.payroll.uk.payroll_processing.fps.repository.FpsSubmissionLogRepository;
import com.payroll.uk.payroll_processing.fps.response.GovTalkResponseDTO;
import com.payroll.uk.payroll_processing.fps.response.GovTalkResponseParser;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.gov.govtalk.cm.envelope.GovTalkMessage;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.IRenvelope;

import java.io.StringWriter;
import java.time.LocalDateTime;

@Service
public class FpsSubmissionService {

    private final FpsXmlBuilder fpsXmlBuilder;
    private final GovTalkMessageBuilder govTalkBuilder;
    private final RestTemplate restTemplate;
    private final FpsSubmissionLogRepository submissionLogRepository;

    public FpsSubmissionService(FpsXmlBuilder fpsXmlBuilder,
                                GovTalkMessageBuilder govTalkBuilder,
                                FpsSubmissionLogRepository submissionLogRepository) {
        this.fpsXmlBuilder = fpsXmlBuilder;
        this.govTalkBuilder = govTalkBuilder;
        this.submissionLogRepository = submissionLogRepository;
        this.restTemplate = new RestTemplate();
    }

    public GovTalkResponseDTO submitFps(FpsSubmissionRequest fpsSubmissionRequest) throws Exception {

        // 1. Build IRenvelope
        IRenvelope irEnvelope = fpsXmlBuilder.buildFpsEnvelope(fpsSubmissionRequest);

        // 2. Prepare GovTalk header
        GovTalkHeaderInfo headerInfo = new GovTalkHeaderInfo();
        headerInfo.senderId = "ISV635";
        headerInfo.authValue = "password";
        headerInfo.taxOfficeNumber = "635";
        headerInfo.taxOfficeReference = "A635";
        headerInfo.messageClass = "HMRC-PAYE-RTI-FPS";
        headerInfo.qualifier = "request";
        headerInfo.function = "submit";
        headerInfo.gatewayTest = true;

        // 3. Build GovTalkMessage
        GovTalkMessage govTalkMessage = govTalkBuilder.buildGovTalkMessage(irEnvelope, headerInfo);

        // 4. Marshal XML
        JAXBContext ctx = JAXBContext.newInstance(GovTalkMessage.class);
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        marshaller.marshal(govTalkMessage, sw);
        String fpsXml = sw.toString();

        // 5. Endpoint
        String hmrcEndpoint = headerInfo.gatewayTest
                ? "https://test-transaction-engine.tax.service.gov.uk/submission"
                : "https://transaction-engine.tax.service.gov.uk/submission";

        // 6. Send POST
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_XML);
        headers.set("Accept", "application/xml");

        HttpEntity<String> requestEntity = new HttpEntity<>(fpsXml, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.postForEntity(hmrcEndpoint, requestEntity, String.class);

        String responseXml = responseEntity.getBody();

        // 7. Parse response
        GovTalkResponseDTO responseDto = GovTalkResponseParser.parse(responseXml);

        // 8. Save log
        FpsSubmissionLog log = new FpsSubmissionLog();
        log.setEmployerRef(headerInfo.taxOfficeNumber + "/" + headerInfo.taxOfficeReference);
        log.setSubmissionXml(fpsXml);
        log.setResponseXml(responseXml);
        log.setCorrelationId(responseDto.getCorrelationId());
        log.setTransactionId(responseDto.getTransactionId());
        log.setStatus(responseDto.getStatus());
        log.setSubmittedAt(LocalDateTime.now());

        submissionLogRepository.save(log);

        return responseDto;
    }
}
