package com.payroll.uk.payroll_processing.fps.builder.header;


import com.payroll.uk.payroll_processing.fps.builder.util.XmlHelper;
import com.payroll.uk.payroll_processing.fps.dto.ir.*;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.*;

import java.util.Objects;

@Component
public class IRHeaderMapper {

    public IRheader mapToIRheader(IRHeaderDTO dto) {
        Objects.requireNonNull(dto, "IRHeaderDTO is required");

        IRheader h = new IRheader();

        // --- Keys ---
        if (dto.getKeys() != null && !dto.getKeys().isEmpty()) {
            IRheader.Keys keys = new IRheader.Keys();
            dto.getKeys().forEach(kdto -> {
                IRheader.Keys.Key k = new IRheader.Keys.Key();
                k.setType(kdto.getType());
                k.setValue(kdto.getValue());
                keys.getKey().add(k);
            });
            h.setKeys(keys);
        }

        // --- PeriodEnd ---
        if (dto.getPeriodEnd() != null) {
            h.setPeriodEnd(XmlHelper.xmlDate(dto.getPeriodEnd()));
        }

        // --- DefaultCurrency ---
        if (dto.getDefaultCurrency() != null) {
            h.setDefaultCurrency(FullPaymentSubmissionISOcurrencyType.fromValue(dto.getDefaultCurrency()));
        }

        // --- IRmark ---
        if (dto.getIrmark() != null) {
            IRheader.IRmark irmark = new IRheader.IRmark();
            irmark.setType("generic");
            irmark.setValue(dto.getIrmark());
            h.setIRmark(irmark);
        }

        // --- Sender ---
        if (dto.getSender() != null) {
            h.setSender(dto.getSender());
        }

        // --- Principal ---
        if (dto.getPrincipal() != null) {
            h.setPrincipal(mapPrincipal(dto.getPrincipal()));
        }

        // --- Agent ---
        if (dto.getAgent() != null) {
            h.setAgent(mapAgent(dto.getAgent()));
        }

        // --- Manifest ---
        if (dto.getManifest() != null) {
            h.setManifest(mapManifest(dto.getManifest()));
        }

        return h;
    }

    // ---------- Principal ----------
    private IRheader.Principal mapPrincipal(PrincipalDTO dto) {
        IRheader.Principal p = new IRheader.Principal();
        if (dto.getContact() != null) {
            p.setContact(mapContact(dto.getContact()));
        }
        return p;
    }

    // Agent is same structure as Principal
    private IRheader.Agent mapAgent(AgentDTO dto) {
        IRheader.Agent a = new IRheader.Agent();
        if (dto.getContact() != null) {
            a.setContact(mapContact(dto.getContact()));
        }
        return a;
    }

    private FullPaymentSubmissionContactDetailsStructure mapContact(ContactDetailsDTO dto) {
        Objects.requireNonNull(dto, "ContactDetailsDTO is required");

        FullPaymentSubmissionContactDetailsStructure c = new FullPaymentSubmissionContactDetailsStructure();

        // --- Name ---
        if (dto.getName() != null) {
            FullPaymentSubmissionContactDetailsStructure.Name n =
                    new FullPaymentSubmissionContactDetailsStructure.Name();
            if (dto.getName().getTitle() != null) n.setTtl(dto.getName().getTitle());
            if (dto.getName().getForenames() != null) n.getFore().addAll(dto.getName().getForenames());
            if (dto.getName().getSurname() != null) n.setSur(dto.getName().getSurname());
            c.setName(n);
        }

        // --- Emails ---
        if (dto.getEmails() != null) {
            dto.getEmails().forEach(e -> {
                FullPaymentSubmissionContactDetailsStructure.Email em =
                        new FullPaymentSubmissionContactDetailsStructure.Email();
                em.setValue(e.getEmail());
                if (e.getType() != null) em.setType(FullPaymentSubmissionWorkHomeType.fromValue(e.getType().toUpperCase()));
                if (e.getPreferred() != null) em.setPreferred(FullPaymentSubmissionYesNoType.fromValue(e.getPreferred() ? "yes" : "no"));
                c.getEmail().add(em);
            });
        }

        // --- Telephones ---
        if (dto.getTelephones() != null) {
            dto.getTelephones().forEach(t -> {
                FullPaymentSubmissionTelephoneStructure tel =
                        new FullPaymentSubmissionTelephoneStructure();
                tel.setNumber(t.getNumber());
                if (t.getExtension() != null) tel.setExtension(t.getExtension());
                if (t.getType() != null) tel.setType(FullPaymentSubmissionWorkHomeType.fromValue(t.getType().toUpperCase()));
                if (t.getMobile() != null) tel.setMobile(FullPaymentSubmissionYesNoType.fromValue(t.getMobile() ? "yes" : "no"));
                if (t.getPreferred() != null) tel.setPreferred(FullPaymentSubmissionYesNoType.fromValue(t.getPreferred() ? "yes" : "no"));
                c.getTelephone().add(tel);
            });
        }

        // --- Faxes ---
        if (dto.getFaxes() != null) {
            dto.getFaxes().forEach(f -> {
                FullPaymentSubmissionTelephoneStructure fx =
                        new FullPaymentSubmissionTelephoneStructure();
                fx.setNumber(f.getNumber());
                if (f.getExtension() != null) fx.setExtension(f.getExtension());
                if (f.getType() != null) fx.setType(FullPaymentSubmissionWorkHomeType.fromValue(f.getType().toUpperCase()));
                if (f.getPreferred() != null) fx.setPreferred(FullPaymentSubmissionYesNoType.fromValue(f.getPreferred() ? "yes" : "no"));
                c.getFax().add(fx);
            });
        }

        return c;
    }


    // ---------- Manifest ----------
    private IRheader.Manifest mapManifest(ManifestDTO dto) {
        IRheader.Manifest m = new IRheader.Manifest();

        if (dto.getContains() != null && dto.getContains().getReferences() != null) {
            IRheader.Manifest.Contains contains = new IRheader.Manifest.Contains();
            dto.getContains().getReferences().forEach(r -> {
                IRheader.Manifest.Contains.Reference ref = new IRheader.Manifest.Contains.Reference();
                ref.setNamespace(r.getNamespace());
                ref.setSchemaVersion(r.getSchemaVersion());
                ref.setTopElementName(r.getTopElementName());
                contains.getReference().add(ref);
            });
            m.setContains(contains);
        }

        return m;
    }
}
