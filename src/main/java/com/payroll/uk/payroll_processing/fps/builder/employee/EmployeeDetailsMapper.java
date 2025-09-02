package com.payroll.uk.payroll_processing.fps.builder.employee;


import com.payroll.uk.payroll_processing.fps.builder.util.XmlHelper;
import com.payroll.uk.payroll_processing.fps.dto.employeeDetails.EmployeeDetailsDTO;
import com.payroll.uk.payroll_processing.fps.dto.employeeDetails.NameDTO;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Objects;

@Component
public class EmployeeDetailsMapper {

    public FullPaymentSubmission.Employee.EmployeeDetails mapToEmployeeDetails(EmployeeDetailsDTO dto) {
        Objects.requireNonNull(dto, "EmployeeDetailsDTO is required");

        FullPaymentSubmission.Employee.EmployeeDetails emp = new FullPaymentSubmission.Employee.EmployeeDetails();

        // --- NINO ---
        if (dto.getEmployeeNINO() != null) {
            emp.setNINO(dto.getEmployeeNINO());
        }

        // --- Name ---
        if (dto.getEmployeeName() != null) {
            FullPaymentSubmission.Employee.EmployeeDetails.Name name = new FullPaymentSubmission.Employee.EmployeeDetails.Name();
            if (dto.getEmployeeName().getEmployeeTtl() != null) {
                name.setTtl(dto.getEmployeeName().getEmployeeTtl());
            }
            if (dto.getEmployeeName().getEmployeeFore() != null) {
                name.getFore().add(dto.getEmployeeName().getEmployeeFore());
            }
            if (dto.getEmployeeName().getEmployeeInitials() != null) {
                name.setInitials(dto.getEmployeeName().getEmployeeInitials());  // Initials
            }
            if (dto.getEmployeeName().getEmployeeSur() != null) {
                name.setSur(dto.getEmployeeName().getEmployeeSur());
            }
            emp.setName(name);
        }


        // --- Address ---
        if (dto.getEmployeeAddress() != null) {
            FullPaymentSubmission.Employee.EmployeeDetails.Address addr =
                    new FullPaymentSubmission.Employee.EmployeeDetails.Address();

            // Map all available lines into the JAXB list
            if (dto.getEmployeeAddress().getAddressLine1() != null) {
                addr.getLine().add(dto.getEmployeeAddress().getAddressLine1());
            }
            if (dto.getEmployeeAddress().getAddressLine2() != null) {
                addr.getLine().add(dto.getEmployeeAddress().getAddressLine2());
            }
            if (dto.getEmployeeAddress().getAddressLine3() != null) {
                addr.getLine().add(dto.getEmployeeAddress().getAddressLine3());
            }
            if (dto.getEmployeeAddress().getAddressLine4() != null) {
                addr.getLine().add(dto.getEmployeeAddress().getAddressLine4());
            }

            // Postcode
            if (dto.getEmployeeAddress().getUkPostcode() != null) {
                addr.setUKPostcode(dto.getEmployeeAddress().getUkPostcode());
            }

            // Foreign Country
            if (dto.getEmployeeAddress().getForeignCountry() != null) {
                addr.setForeignCountry(dto.getEmployeeAddress().getForeignCountry());
            }

            emp.setAddress(addr);
        }


        // --- BirthDate ---
        if (dto.getEmployeeBirthDate() != null) {
            XMLGregorianCalendar xmlDate = XmlHelper.xmlDate(dto.getEmployeeBirthDate());
            emp.setBirthDate(xmlDate);
        }

        // --- Gender ---
        if (dto.getEmployeeGender() != null) {
            String gender = dto.getEmployeeGender().equalsIgnoreCase("male") ? "M" : "F";
            emp.setGender(gender); // typically "M", "F", or "N"
        }


        // --- Passport Number ---
        if (dto.getEmployeePassportNumber() != null) {
            emp.setPassportNumber(dto.getEmployeePassportNumber());
        }

        // --- Partner Details ---
        if (dto.getEmployeePartnerDetails() != null) {
            FullPaymentSubmission.Employee.EmployeeDetails.PartnerDetails partner = new FullPaymentSubmission.Employee.EmployeeDetails.PartnerDetails();
            if (dto.getEmployeePartnerDetails().getEmployeePartnerNINO() != null) {
                partner.setNINO(dto.getEmployeePartnerDetails().getEmployeePartnerNINO());
            }
            if (dto.getEmployeePartnerDetails().getEmployeePartnerName() != null) {
                FullPaymentSubmission.Employee.EmployeeDetails.PartnerDetails.Name name =
                        new FullPaymentSubmission.Employee.EmployeeDetails.PartnerDetails.Name();
                mapName(dto.getEmployeePartnerDetails().getEmployeePartnerName());
                partner.setName(name);
            }
            emp.setPartnerDetails(partner);
        }

        return emp;
    }

    private FullPaymentSubmission.Employee.EmployeeDetails.Name mapName(NameDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.EmployeeDetails.Name name =
                new FullPaymentSubmission.Employee.EmployeeDetails.Name();

        if (dto.getEmployeeTtl() != null) {
            name.setTtl(dto.getEmployeeTtl());
        }
        if (dto.getEmployeeFore() != null) {
            name.getFore().add(dto.getEmployeeFore());
        }
        if (dto.getEmployeeInitials() != null) {
            name.setInitials(dto.getEmployeeInitials());  // Initials
        }
        if (dto.getEmployeeSur() != null) {
            name.setSur(dto.getEmployeeSur());
        }

        return name;
    }

}
