package com.payroll.uk.payroll_processing.fps.builder.employee;


import com.payroll.uk.payroll_processing.fps.builder.util.XmlHelper;
import com.payroll.uk.payroll_processing.fps.dto.employment.*;
import org.springframework.stereotype.Component;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmission;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmissionYesNoType;
import uk.gov.govtalk.taxation.paye.rti.fullpaymentsubmission._25_26._1.FullPaymentSubmissionYesType;

import java.util.ArrayList;
import java.util.List;

@Component
public class EmploymentMapper {

    public FullPaymentSubmission.Employee.Employment mapToEmployment(EmploymentDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment emp =
                new FullPaymentSubmission.Employee.Employment();

        if (dto.getEmploymentOffPayrollWorker() != null) {
            emp.setOffPayrollWorker(FullPaymentSubmissionYesType.fromValue(dto.getEmploymentOffPayrollWorker()));
        }

        if (dto.getEmploymentOccPenInd() != null) {
            emp.setOccPenInd(FullPaymentSubmissionYesType.fromValue(dto.getEmploymentOccPenInd()));
        }

        if (dto.getEmploymentDirectorsNIC() != null) {
            emp.setDirectorsNIC(dto.getEmploymentDirectorsNIC());
        }

        if (dto.getEmploymentTaxWkOfApptOfDirector() != null) {
            emp.setTaxWkOfApptOfDirector(dto.getEmploymentTaxWkOfApptOfDirector());
        }

        if (dto.getEmploymentStarter() != null) {
            emp.setStarter(mapStarter(dto.getEmploymentStarter()));
        }

        if (dto.getEmployeeWorkplacePostcode() != null) {
            emp.setEmployeeWorkplacePostcode(dto.getEmployeeWorkplacePostcode());
        }

        if (dto.getEmploymentPayrollId() != null) {
            emp.setPayId(dto.getEmploymentPayrollId());
        }

        if (dto.getEmploymentPayrollIdChanged() != null) {
            emp.setPayIdChgd(mapPayrollIdChanged(dto.getEmploymentPayrollIdChanged()));
        }

        if (dto.getPaymentToANonIndividual() != null) {
            emp.setPaymentToANonIndividual(FullPaymentSubmissionYesType.valueOf(dto.getPaymentToANonIndividual()));
        }

        if (dto.getIrregularPaymentIndicator() != null) {
            emp.setIrrEmp(FullPaymentSubmissionYesType.valueOf(dto.getIrregularPaymentIndicator()));
        }

        if (dto.getEmploymentLeavingDate() != null) {
            emp.setLeavingDate(XmlHelper.xmlDate(dto.getEmploymentLeavingDate()));
        }

        if (dto.getEmploymentFiguresToDate() != null) {
            emp.setFiguresToDate(mapFiguresToDate(dto.getEmploymentFiguresToDate()));
        }

        if (dto.getEmploymentPayment() != null) {
            emp.setPayment(mapPayment(dto.getEmploymentPayment()));
        }

        // --- NI Letters and Values ---
        if (dto.getEmploymentNILettersAndValues() != null && !dto.getEmploymentNILettersAndValues().isEmpty()) {
            dto.getEmploymentNILettersAndValues().forEach(niDto -> {
                emp.getNIlettersAndValues().add(mapNILettersAndValues(niDto));
            });
        }




        return emp;
    }


    private FullPaymentSubmission.Employee.Employment.Starter mapStarter(StarterDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Starter starter =
                new FullPaymentSubmission.Employee.Employment.Starter();

        if (dto.getEmploymentStartDate() != null) {
            // Convert String to XMLGregorianCalendar if HMRC expects a date
            starter.setStartDate(XmlHelper.xmlDate(dto.getEmploymentStartDate()));
        }

        if (dto.getEmploymentStartDec() != null) {
            starter.setStartDec(dto.getEmploymentStartDec());
        }

        if (dto.getEmploymentStudentLoan() != null) {
            starter.setStudentLoan(FullPaymentSubmissionYesType.fromValue(dto.getEmploymentStudentLoan()));
        }

        if (dto.getEmploymentPostgradLoan() != null) {
            starter.setPostgradLoan(FullPaymentSubmissionYesType.fromValue(dto.getEmploymentPostgradLoan()));
        }

        if (dto.getEmploymentSeconded() != null) {
            starter.setSeconded(mapSeconded(dto.getEmploymentSeconded()));
        }

        if (dto.getEmploymentOccPension() != null) {
            starter.setOccPension(mapOccPension(dto.getEmploymentOccPension()));
        }

        if (dto.getEmploymentStatePension() != null) {
            starter.setStatePension(mapStatePension(dto.getEmploymentStatePension()));
        }

        return starter;
    }

    private FullPaymentSubmission.Employee.Employment.Starter.Seconded mapSeconded(SecondedDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Starter.Seconded sec =
                new FullPaymentSubmission.Employee.Employment.Starter.Seconded();

       if (dto.getSecondedStay183DaysOrMore()!=null){
           sec.setStay183DaysOrMore(FullPaymentSubmissionYesType.fromValue(dto.getSecondedStay183DaysOrMore()));
       }
       if (dto.getSecondedStayLessThan183Days()!=null){
           sec.setStayLessThan183Days(FullPaymentSubmissionYesType.fromValue(dto.getSecondedStayLessThan183Days()));
       }
       if (dto.getSecondedInOutUk()!=null){
           sec.setInOutUK(FullPaymentSubmissionYesType.fromValue(dto.getSecondedInOutUk()));
       }
       if (dto.getSecondedEEACitizen()!=null){
           sec.setEEACitizen(FullPaymentSubmissionYesType.fromValue(dto.getSecondedEEACitizen()));
       }
       if (dto.getSecondedEPM6()!=null){
              sec.setEPM6(FullPaymentSubmissionYesType.fromValue(dto.getSecondedEPM6()));
       }

        return sec;
    }

    private FullPaymentSubmission.Employee.Employment.Starter.OccPension mapOccPension(OccAndStatePensionDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Starter.OccPension pension =
                new FullPaymentSubmission.Employee.Employment.Starter.OccPension();

        if (dto.getPensionAmount()!= null) {
            pension.setAmount(dto.getPensionAmount());
        }
        if (dto.getPensionBereaved()!=null){
            pension.setBereaved(FullPaymentSubmissionYesType.fromValue(dto.getPensionBereaved()));
        }

        return pension;
    }

    private FullPaymentSubmission.Employee.Employment.Starter.StatePension mapStatePension(OccAndStatePensionDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Starter.StatePension statePension =
                new FullPaymentSubmission.Employee.Employment.Starter.StatePension();

        if (dto.getPensionAmount()!= null) {
            statePension.setAmount(dto.getPensionAmount());
        }
        if (dto.getPensionBereaved()!=null){
            statePension.setBereaved(FullPaymentSubmissionYesType.fromValue(dto.getPensionBereaved()));
        }

        return statePension;
    }

    private FullPaymentSubmission.Employee.Employment.PayIdChgd mapPayrollIdChanged(PayrollIdChangedDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.PayIdChgd mapped =
                new FullPaymentSubmission.Employee.Employment.PayIdChgd();

        if (dto.getPayrollIdChangedIndicator() != null) {
            mapped.setPayrollIdChangedIndicator(FullPaymentSubmissionYesType.fromValue(dto.getPayrollIdChangedIndicator()));
        }

        if (dto.getOldPayrollId() != null) {
            mapped.setOldPayrollId(dto.getOldPayrollId());
        }

        return mapped;
    }

    private FullPaymentSubmission.Employee.Employment.FiguresToDate mapFiguresToDate(FiguresToDateDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.FiguresToDate mapped =
                new FullPaymentSubmission.Employee.Employment.FiguresToDate();

        if (dto.getTaxablePayTD() != null) {
            mapped.setTaxablePay(dto.getTaxablePayTD());
        }
        if (dto.getTotalTaxTD() != null) {
            mapped.setTotalTax(dto.getTotalTaxTD());
        }
        if (dto.getStudentLoansTD() != null) {
            mapped.setStudentLoansTD(dto.getStudentLoansTD());
        }
        if (dto.getPostGradLoansTD() != null) {
            mapped.setPostgradLoansTD(dto.getPostGradLoansTD());
        }
        if (dto.getBenefitsTaxedViaPayrollYTD() != null) {
            mapped.setBenefitsTaxedViaPayrollYTD(dto.getBenefitsTaxedViaPayrollYTD());
        }
        if (dto.getEmployeePensionContributionsPaidYTD() != null) {
            mapped.setEmpeePenContribnsPaidYTD(dto.getEmployeePensionContributionsPaidYTD());
        }
        if (dto.getEmployeePensionContributionsNotPaidYTD() != null) {
            mapped.setEmpeePenContribnsNotPaidYTD(dto.getEmployeePensionContributionsNotPaidYTD());
        }

        return mapped;
    }


    private FullPaymentSubmission.Employee.Employment.Payment mapPayment(PaymentDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Payment mapped =
                new FullPaymentSubmission.Employee.Employment.Payment();

        // --- Basic fields ---
        if (dto.getPayFrequency() != null) mapped.setPayFreq(dto.getPayFrequency());
        if (dto.getPmtDate() != null) mapped.setPmtDate(XmlHelper.xmlDate(dto.getPmtDate()));
        if (dto.getLateReason() != null) mapped.setLateReason(dto.getLateReason());
        if (dto.getWeekNo() != null) mapped.setWeekNo(dto.getWeekNo());
        if (dto.getMonthNo() != null) mapped.setMonthNo(dto.getMonthNo());
        if (dto.getPeriodsCovered() > 0) mapped.setPeriodsCovered(dto.getPeriodsCovered());
        if (dto.getAggregatedEarnings() != null) mapped.setAggregatedEarnings(FullPaymentSubmissionYesType.valueOf(dto.getAggregatedEarnings()));
        if (dto.getPmtAfterLeaving() != null) mapped.setPmtAfterLeaving(FullPaymentSubmissionYesType.valueOf(dto.getPmtAfterLeaving()));
        if (dto.getHoursWorked() != null) mapped.setHoursWorked(dto.getHoursWorked());

        if (dto.getTaxCode() != null) {
            TaxCodeDTO tcDto = dto.getTaxCode();

            FullPaymentSubmission.Employee.Employment.Payment.TaxCode tc =
                    new FullPaymentSubmission.Employee.Employment.Payment.TaxCode();

            // value -> the actual tax code (e.g., "1257L")
            tc.setValue(tcDto.getTaxCode());

            // attribute: BasisNonCumulative (expects Yes/No enum)
            if (tcDto.getBasisNonCumulative() != null && tcDto.getBasisNonCumulative()) {
                // Only set if true → maps to YES
                tc.setBasisNonCumulative(FullPaymentSubmissionYesType.YES);
            }
            // attribute: TaxRegime (expects "S" or "C" as string in schema)
            if (tcDto.getTaxRegime() != null) {
                tc.setTaxRegime(tcDto.getTaxRegime());
            }
             mapped.setTaxCode(tc);

        }

        // --- Monetary values ---
        if (dto.getTaxablePay() != null) mapped.setTaxablePay(dto.getTaxablePay());
        if (dto.getNonTaxOrNICPmt() != null) mapped.setNonTaxOrNICPmt(dto.getNonTaxOrNICPmt());
        if (dto.getDeductionsFromNetPay() != null) mapped.setDednsFromNetPay(dto.getDeductionsFromNetPay());
        if (dto.getPayAfterStatutoryDeductions() != null) mapped.setPayAfterStatDedns(dto.getPayAfterStatutoryDeductions());
        if (dto.getBenefitsTaxedViaPayroll() != null) mapped.setBenefitsTaxedViaPayroll(dto.getBenefitsTaxedViaPayroll());
        if (dto.getClass1ANICsYTD() != null) mapped.setClass1ANICsYTD(dto.getClass1ANICsYTD());
        if (dto.getEmployeePensionContributions() != null) mapped.setEmpeePenContribnsPaid(dto.getEmployeePensionContributions());
        if (dto.getItemsSubjectToClass1NIC() != null) mapped.setItemsSubjectToClass1NIC(dto.getItemsSubjectToClass1NIC());
        if (dto.getEmployeePensionContributionsNotPaid() != null) mapped.setEmpeePenContribnsNotPaid(dto.getEmployeePensionContributionsNotPaid());

        // --- Loans and Tax ---
        if (dto.getStudentLoanRecovered() != null) {
            mapped.setStudentLoanRecovered(mapStudentLoanRecovered(dto.getStudentLoanRecovered()));
        }

        if (dto.getPostgraduateLoanRecovered() != null) mapped.setPostgradLoanRecovered(dto.getPostgraduateLoanRecovered());
        if (dto.getTaxDeductedOrRefunded() != null) mapped.setTaxDeductedOrRefunded(dto.getTaxDeductedOrRefunded());

        // --- Absences ---
        if (dto.getOnStrike() != null) mapped.setOnStrike(FullPaymentSubmissionYesType.fromValue(dto.getOnStrike()));
        if (dto.getUnpaidAbsence() != null) mapped.setUnpaidAbsence(FullPaymentSubmissionYesType.fromValue(dto.getUnpaidAbsence()));

        // --- Statutory Payments ---
        if (dto.getSmpytd() != null) mapped.setSMPYTD(dto.getSmpytd());
        if (dto.getSppytd() != null) mapped.setSPPYTD(dto.getSppytd());
        if (dto.getSapytd() != null) mapped.setSAPYTD(dto.getSapytd());
        if (dto.getShPPYTD() != null) mapped.setShPPYTD(dto.getShPPYTD());
        if (dto.getSpbpytd() != null) mapped.setSPBPYTD(dto.getSpbpytd());
        if (dto.getSncpytd() != null) mapped.setSNCPYTD(dto.getSncpytd());

        // --- Trivial Commutation ---
        if (dto.getTrivialCommutationPayments() != null) {
            List<FullPaymentSubmission.Employee.Employment.Payment.TrivialCommutationPayment> tcps =
                    mapTrivialCommutationPayments(dto.getTrivialCommutationPayments());
            if (tcps != null) {
                mapped.getTrivialCommutationPayment().addAll(tcps);
            }
        }


        // --- Car Benefits ---
        if (dto.getCarBenefits() != null) {
            FullPaymentSubmission.Employee.Employment.Payment.Benefits.Car car =
                    mapCarBenefits(dto.getCarBenefits());
            if (car != null) {
                mapped.getBenefits().getCar().add(car); // multiple cars possible
            }
        }
        // --- Flexible Drawdown ---
        if (dto.getFlexibleDrawdown() != null) {
            mapped.setFlexibleDrawdown(mapFlexibleDrawdown(dto.getFlexibleDrawdown()));
        }


        return mapped;
    }

    private FullPaymentSubmission.Employee.Employment.Payment.StudentLoanRecovered
    mapStudentLoanRecovered(StudentLoanRecoveredDTO dto) {

        if (dto == null || dto.getAmount() == null) {
            return null; // optional element
        }

        FullPaymentSubmission.Employee.Employment.Payment.StudentLoanRecovered slr =
                new FullPaymentSubmission.Employee.Employment.Payment.StudentLoanRecovered();

        // Set the value (amount)
        slr.setValue(dto.getAmount());

        // Set the PlanType attribute (required)
        if (dto.getPlanType() != null) {
            slr.setPlanType(dto.getPlanType()); // "01", "02", "04"
        } else {
            throw new IllegalArgumentException("PlanType is required for StudentLoanRecovered");
        }

        return slr;
    }

    private List<FullPaymentSubmission.Employee.Employment.Payment.TrivialCommutationPayment>
    mapTrivialCommutationPayments(List<TrivialCommutationPaymentDTO> dtos) {

        if (dtos == null || dtos.isEmpty()) {
            return null;
        }

        // Ensure uniqueness of type
        long distinctCount = dtos.stream()
                .map(TrivialCommutationPaymentDTO::getType)
                .distinct()
                .count();

        if (distinctCount != dtos.size()) {
            throw new IllegalArgumentException(
                    "Each TrivialCommutationPayment type (A,B,C) must be unique within a Payment");
        }

        List<FullPaymentSubmission.Employee.Employment.Payment.TrivialCommutationPayment> payments =
                new ArrayList<>();

        dtos.forEach(dto -> {
            if (dto.getAmount() != null && dto.getType() != null) {
                FullPaymentSubmission.Employee.Employment.Payment.TrivialCommutationPayment tcp =
                        new FullPaymentSubmission.Employee.Employment.Payment.TrivialCommutationPayment();

                tcp.setValue(dto.getAmount());
                tcp.setType(dto.getType()); // "A", "B", or "C"

                payments.add(tcp);
            }
        });

        return payments;
    }
    private FullPaymentSubmission.Employee.Employment.Payment.Benefits.Car mapCarBenefits(CarBenefits dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Payment.Benefits.Car car =
                new FullPaymentSubmission.Employee.Employment.Payment.Benefits.Car();

        if (dto.getCarModel() != null) car.setMake(dto.getCarModel());
        if (dto.getCarFirstRegisteredDate() != null)
            car.setFirstRegd(XmlHelper.xmlDate(dto.getCarFirstRegisteredDate()));
        if (dto.getCarCO2() != null) car.setCO2(dto.getCarCO2());
        if (dto.getCarZeroEmissionMileage() != null)
            car.setZeroEmissionsMileage(dto.getCarZeroEmissionMileage());
        if (dto.getCarFuelType() != null) car.setFuel(dto.getCarFuelType());
        if (dto.getCarIdentifier() != null) car.setID(dto.getCarIdentifier().toPlainString());
        if (dto.getCarAmendmentIndicator() != null) car.setAmendment(FullPaymentSubmissionYesNoType.fromValue(dto.getCarAmendmentIndicator()));
        if (dto.getCarPrice() != null) car.setPrice(dto.getCarPrice());
        if (dto.getCarAvailableFromDate() != null)
            car.setAvailFrom(XmlHelper.xmlDate(dto.getCarAvailableFromDate()));
        if (dto.getCarCashEquivalent() != null) car.setCashEquiv(dto.getCarCashEquivalent());
        if (dto.getCarAvailableTo() != null) car.setAvailTo(XmlHelper.xmlDate(dto.getCarAvailableTo()));

        // --- Fuel Benefits ---
        if (dto.getFreeFuelProvidedDate() != null
                || dto.getFreeFuelCashEquivalent() != null
                || dto.getFreeFuelWithDrawnDate() != null) {

            FullPaymentSubmission.Employee.Employment.Payment.Benefits.Car.FreeFuel fuel =
                    new FullPaymentSubmission.Employee.Employment.Payment.Benefits.Car.FreeFuel();

            if (dto.getFreeFuelProvidedDate() != null)
                fuel.setProvided(XmlHelper.xmlDate(dto.getFreeFuelProvidedDate()));
            if (dto.getFreeFuelCashEquivalent() != null)
                fuel.setCashEquiv(dto.getFreeFuelCashEquivalent());
            if (dto.getFreeFuelWithDrawnDate() != null)
                fuel.setWithdrawn(XmlHelper.xmlDate(dto.getFreeFuelWithDrawnDate()));

            car.setFuel(fuel.toString());
        }

        return car;
    }

    private FullPaymentSubmission.Employee.Employment.Payment.FlexibleDrawdown mapFlexibleDrawdown(FlexibleDrawdownDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.Payment.FlexibleDrawdown flex =
                new FullPaymentSubmission.Employee.Employment.Payment.FlexibleDrawdown();

        if (dto.getFlexiblyAccessingPensionRights() != null) {
            flex.setFlexiblyAccessingPensionRights(FullPaymentSubmissionYesType.fromValue(dto.getFlexiblyAccessingPensionRights()));
        }
        if (dto.getPensionDeathBenefit() != null) {
            flex.setPensionDeathBenefit(FullPaymentSubmissionYesType.fromValue(dto.getPensionDeathBenefit()));
        }
        if (dto.getSeriousIllHealthLumpSum() != null) {
            flex.setSeriousIllHealthLumpSum(FullPaymentSubmissionYesType.fromValue(dto.getSeriousIllHealthLumpSum()));
        }
        if (dto.getPensionCommencementExcessLumpSum() != null) {
            flex.setPensionCommencementExcessLumpSum(FullPaymentSubmissionYesType.fromValue(dto.getPensionCommencementExcessLumpSum()));
        }
        if (dto.getStandAloneLumpSum() != null) {
            flex.setStandAloneLumpSum(FullPaymentSubmissionYesType.fromValue(dto.getStandAloneLumpSum()));
        }
        if (dto.getTaxablePayment() != null) {
            flex.setTaxablePayment(dto.getTaxablePayment());
        }
        if (dto.getNontaxablePayment() != null) {
            flex.setNontaxablePayment(dto.getNontaxablePayment());
        }

        return flex;
    }


    private FullPaymentSubmission.Employee.Employment.NIlettersAndValues mapNILettersAndValues(NILettersAndValuesDTO dto) {
        if (dto == null) return null;

        FullPaymentSubmission.Employee.Employment.NIlettersAndValues ni =
                new FullPaymentSubmission.Employee.Employment.NIlettersAndValues();

        if (dto.getNiLetter() != null) ni.setNIletter(dto.getNiLetter());
        if (dto.getGrossEarningsForNICsInPd() != null) ni.setGrossEarningsForNICsInPd(dto.getGrossEarningsForNICsInPd());
        if (dto.getGrossEarningsForNICsYTD() != null) ni.setGrossEarningsForNICsYTD(dto.getGrossEarningsForNICsYTD());
        if (dto.getAtLELYTD() != null) ni.setAtLELYTD(dto.getAtLELYTD());
        if (dto.getLelToPTYTD() != null) ni.setLELtoPTYTD(dto.getLelToPTYTD());
        if (dto.getPtToUELYTD() != null) ni.setPTtoUELYTD(dto.getPtToUELYTD());
        if (dto.getTotalEmployerNICInPd() != null) ni.setTotalEmpNICInPd(dto.getTotalEmployerNICInPd());
        if (dto.getTotalEmployerNICYTD() != null) ni.setTotalEmpNICYTD(dto.getTotalEmployerNICYTD());
        if (dto.getEmployeeNIContributionsInPd() != null) ni.setEmpeeContribnsInPd(dto.getEmployeeNIContributionsInPd());
        if (dto.getEmployeeNIContributionsYTD() != null) ni.setEmpeeContribnsYTD(dto.getEmployeeNIContributionsYTD());

        return ni;
    }










}
