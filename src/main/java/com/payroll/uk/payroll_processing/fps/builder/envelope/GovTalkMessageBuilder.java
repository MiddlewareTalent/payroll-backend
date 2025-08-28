package com.payroll.uk.payroll_processing.fps.builder.envelope;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import uk.gov.govtalk.cm.envelope.GovTalkMessage;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.IRenvelope;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.IRheader;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.GregorianCalendar;

public class GovTalkMessageBuilder {

    public GovTalkMessage buildGovTalkMessage(IRenvelope irEnvelope, GovTalkHeaderInfo info) throws Exception {

        if (irEnvelope == null) throw new IllegalArgumentException("IRenvelope required");
        if (info == null) throw new IllegalArgumentException("Header info required");

        // ---- Compute IRmark ----
        IRheader header = irEnvelope.getIRheader();
        String irXml = marshalToString(irEnvelope);
        String irmark = computeIRMark(irXml);
        IRheader.IRmark mark = new IRheader.IRmark();
        mark.setType("generic");
        mark.setValue(irmark);
        header.setIRmark(mark);

        // ---- Build GovTalkMessage ----
        GovTalkMessage message = new GovTalkMessage();
        message.setEnvelopeVersion("2.0");

        // --- Header ---
        GovTalkMessage.Header h = new GovTalkMessage.Header();
        GovTalkMessage.Header.MessageDetails md = new GovTalkMessage.Header.MessageDetails();
        md.setClazz(info.messageClass); // JAXB generated uses setClazz
        md.setQualifier(info.qualifier);
        md.setFunction(info.function);
        md.setGatewayTest(info.gatewayTest ? BigInteger.ONE : BigInteger.ZERO);
        md.setTransformation(info.transformation);
        h.setMessageDetails(md);

        // SenderDetails
        GovTalkMessage.Header.SenderDetails sd = new GovTalkMessage.Header.SenderDetails();
        GovTalkMessage.Header.SenderDetails.IDAuthentication ida = new GovTalkMessage.Header.SenderDetails.IDAuthentication();
        ida.setSenderID(info.senderId);

        GovTalkMessage.Header.SenderDetails.IDAuthentication.Authentication auth =
                new GovTalkMessage.Header.SenderDetails.IDAuthentication.Authentication();
        auth.setMethod(info.authMethod);
        auth.setRole(info.authRole);
        auth.setValue(info.authValue);
        ida.getAuthentication().add(auth);
        sd.setIDAuthentication(ida);
        h.setSenderDetails(sd);
        message.setHeader(h);

        // --- GovTalkDetails ---
        GovTalkMessage.GovTalkDetails gtd = new GovTalkMessage.GovTalkDetails();

        // Keys
        GovTalkMessage.GovTalkDetails.Keys keys = new GovTalkMessage.GovTalkDetails.Keys();
        GovTalkMessage.GovTalkDetails.Keys.Key k1 = new GovTalkMessage.GovTalkDetails.Keys.Key();
        k1.setType("TaxOfficeNumber");
        k1.setValue(info.taxOfficeNumber);
        GovTalkMessage.GovTalkDetails.Keys.Key k2 = new GovTalkMessage.GovTalkDetails.Keys.Key();
        k2.setType("TaxOfficeReference");
        k2.setValue(info.taxOfficeReference);
        keys.getKey().add(k1);
        keys.getKey().add(k2);
        gtd.setKeys(keys);

        // TargetDetails
        GovTalkMessage.GovTalkDetails.TargetDetails td = new GovTalkMessage.GovTalkDetails.TargetDetails();
        td.getOrganisation().add(info.organisation); // List<String>
        gtd.setTargetDetails(td);

        // ChannelRouting
        GovTalkMessage.GovTalkDetails.ChannelRouting cr = new GovTalkMessage.GovTalkDetails.ChannelRouting();
        GovTalkMessage.GovTalkDetails.ChannelRouting.Channel c = new GovTalkMessage.GovTalkDetails.ChannelRouting.Channel();
        c.setURI(info.channelUri);
        c.setProduct(info.channelProduct);
        c.setVersion(info.channelVersion);

        // Timestamp → XMLGregorianCalendar
        OffsetDateTime ts = info.timestamp != null ? info.timestamp : OffsetDateTime.now(ZoneOffset.UTC);
        GregorianCalendar cal = GregorianCalendar.from(ts.toZonedDateTime());
        XMLGregorianCalendar xmlCal = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        cr.setTimestamp(xmlCal);

        cr.setChannel(c);
        gtd.getChannelRouting().add(cr);

        message.setGovTalkDetails(gtd);

        // --- Body ---
        GovTalkMessage.Body body = new GovTalkMessage.Body();
        body.getAny().add(irEnvelope);
        message.setBody(body);

        return message;
    }

    private String marshalToString(IRenvelope ir) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(IRenvelope.class);
        Marshaller m = ctx.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter sw = new StringWriter();
        m.marshal(ir, sw);
        return sw.toString();
    }

    private String computeIRMark(String xml) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(xml.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(digest);
    }
}
