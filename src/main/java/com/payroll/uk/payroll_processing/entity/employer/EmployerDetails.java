package com.payroll.uk.payroll_processing.entity.employer;

import com.payroll.uk.payroll_processing.entity.BankDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "employer_details")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(name = "company_details")
    private CompanyDetails companyDetails;

    @Column(name = "employer_id",unique = true)
    private String employerId;



    @NotBlank(message = "Employer name cannot be blank")
    @Column(name = "employerName")
//    @Size(min = 2, max = 100, message = "Employer name must be between 2-100 characters")
//    @Pattern(regexp = "^[a-zA-Z0-9 \\-\\'&,.]*$", message = "Employer name contains invalid characters")
    private String employerName;

//    @NotBlank(message = "Address cannot be blank")
//    @Size(max = 200)
    @Column(name = "employerAddress")
    private String employerAddress;

//    @NotBlank(message = "Postcode cannot be blank")
//    @Pattern(regexp = "^([A-Za-z][A-Ha-hJ-Yj-y]?[0-9][A-Za-z0-9]? ?[0-9][A-Za-z]{2}|[Gg][Ii][Rr] ?0[Aa]{2})$",
//            message = "Invalid UK postcode format")
    @Column(name = "employerPostCode")
    private String employerPostCode;


    @NotBlank(message = "Telephone number cannot be blank")
//    @Pattern(regexp = "^(\\+44|0)[1-9]\\d{8,9}$",
//            message = "Invalid UK telephone number format")
    @Column(name = "employerTelephone")
    private String employerTelephone;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Column(name="employerEmail",unique = true)
    private String employerEmail;

    @Embedded
    @Column(name = "tax_office")
    private TaxOffice taxOffice;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "bank_details_id", referencedColumnName = "id")
    @ToString.Exclude
    private BankDetails bankDetails;

    @Embedded
    @Column(name = "terms")
    private Terms terms;

    @Embedded
    @Column(name = "otherEmployerDetails")
    private OtherEmployerDetails otherEmployerDetails;




    public void setBankDetails(BankDetails bankDetails) {
        this.bankDetails = bankDetails;
        if (bankDetails != null) {
            bankDetails.setEmployerDetails(this);
        }
    }





}



