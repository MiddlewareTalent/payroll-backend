package com.payroll.uk.payroll_processing.fps.builder;


import com.payroll.uk.payroll_processing.fps.builder.util.XmlHelper;
import com.payroll.uk.payroll_processing.fps.dto.FinalSubmissionDTO;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmissionYesNoType;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmissionYesType;


import java.util.Objects;

@Component
public class FinalSubmissionMapper {

    public  FullPaymentSubmission.FinalSubmission mapToFinalSubmission(FinalSubmissionDTO dto) {
        Objects.requireNonNull(dto, "FinalSubmissionDTO is required");

        FullPaymentSubmission.FinalSubmission fs = new FullPaymentSubmission.FinalSubmission();

        // BecauseSchemeCeased
        if ("yes".equalsIgnoreCase(dto.getBecauseSchemeCeased())) {
            fs.setBecauseSchemeCeased(FullPaymentSubmissionYesType.YES);
        }

        // DateSchemeCeased
        if (dto.getDateSchemeCeased() != null) {
            fs.setDateSchemeCeased(XmlHelper.xmlDate(dto.getDateSchemeCeased()));
        }

        // ForYear
        if ("yes".equalsIgnoreCase(dto.getForYear())) {
            fs.setForYear(FullPaymentSubmissionYesType.YES);
        }

        return fs;
    }


    public FullPaymentSubmissionYesNoType toYesNo(String value) {
        // normalize string (case insensitive: "yes", "YES", "No", "NO")
        if ("yes".equalsIgnoreCase(value)) {
            return FullPaymentSubmissionYesNoType.YES;
        } else if ("no".equalsIgnoreCase(value)) {
            return FullPaymentSubmissionYesNoType.NO;
        }
        throw new IllegalArgumentException("Invalid Yes/No value: " + value);
    }
}
