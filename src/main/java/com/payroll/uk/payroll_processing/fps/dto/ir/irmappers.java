package com.payroll.uk.payroll_processing.fps.dto.ir;

public class irmappers {



   /* // ---------- Contact ----------
    private IRheader.Principal mapContact(ContactDetailsDTO dto) {
        IRheader.Contact c = new IRheader.Contact();

        // Name
        if (dto.getName() != null) {
            IRheader.Contact.Name n = new IRheader.Contact.Name();
            if (dto.getName().getTitle() != null) n.setTtl(dto.getName().getTitle());
            if (dto.getName().getForenames() != null) n.getFore().addAll(dto.getName().getForenames());
            if (dto.getName().getSurname() != null) n.setSur(dto.getName().getSurname());
            c.setName(n);
        }

        // Emails
        if (dto.getEmails() != null) {
            dto.getEmails().forEach(e -> {
                IRheader.Contact.Email em = new IRheader.Contact.Email();
                em.setValue(e.getEmail());
                if (e.getType() != null) em.setType(e.getType());
                if (e.getPreferred() != null) em.setPreferred(e.getPreferred() ? "yes" : "no");
                c.getEmail().add(em);
            });
        }

        // Telephones
        if (dto.getTelephones() != null) {
            dto.getTelephones().forEach(t -> {
                IRheader.Contact.Telephone tel = new IRheader.Contact.Telephone();
                tel.setValue(t.getNumber());
                if (t.getExtension() != null) tel.setExtension(t.getExtension());
                if (t.getType() != null) tel.setType(t.getType());
                if (t.getMobile() != null) tel.setMobile(t.getMobile() ? "yes" : "no");
                if (t.getPreferred() != null) tel.setPreferred(t.getPreferred() ? "yes" : "no");
                c.getTelephone().add(tel);
            });
        }

        // Faxes
        if (dto.getFaxes() != null) {
            dto.getFaxes().forEach(f -> {
                IRheader.Contact.Fax fx = new IRheader.Contact.Fax();
                fx.setValue(f.getNumber());
                if (f.getExtension() != null) fx.setExtension(f.getExtension());
                if (f.getType() != null) fx.setType(f.getType());
                if (f.getPreferred() != null) fx.setPreferred(f.getPreferred() ? "yes" : "no");
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
    }*/
}
