package com.payroll.uk.payroll_processing.fps.builder.envelope;

import java.time.OffsetDateTime;

public class GovTalkHeaderInfo {
    public String messageClass = "HMRC-PAYE-RTI-FPS";
    public String qualifier = "request";
    public String function = "submit";
    public String transformation = "XML";
    public boolean gatewayTest = true; // 1 = test
    public String senderId;
    public String authMethod = "clear";
    public String authRole = "principal";
    public String authValue;
    public String taxOfficeNumber;
    public String taxOfficeReference;
    public String organisation = "IR";
    public String channelUri;
    public String channelProduct;
    public String channelVersion;
    public OffsetDateTime timestamp;
}
