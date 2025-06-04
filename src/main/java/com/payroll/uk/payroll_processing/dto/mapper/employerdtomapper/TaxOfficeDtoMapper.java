package com.payroll.uk.payroll_processing.dto.mapper.employerdtomapper;


import com.payroll.uk.payroll_processing.dto.employerdto.TaxOfficeDto;
import com.payroll.uk.payroll_processing.entity.employer.TaxOffice;
import org.springframework.stereotype.Component;


@Component
public class TaxOfficeDtoMapper {
    public TaxOfficeDto mapToDto(TaxOffice taxOffice) {
        TaxOfficeDto dto = new TaxOfficeDto();
        dto.setPayeReference(taxOffice.getPayeReference());
        dto.setAccountsOfficeReference(taxOffice.getAccountsOfficeReference());
        dto.setPaymentMethod(taxOffice.getPaymentMethod());
        dto.setUniqueTaxRef(taxOffice.getUniqueTaxRef());
        dto.setCorporationTaxRef(taxOffice.getCorporationTaxRef());
        dto.setPayrollGivingRef(taxOffice.getPayrollGivingRef());
        dto.setSerQualifiedThisYear(taxOffice.getSerQualifiedThisYear());
        dto.setSerQualifiedLastYear(taxOffice.getSerQualifiedLastYear());
        dto.setNoRtiDueWarnings(taxOffice.getNoRtiDueWarnings());
        dto.setClaimNICAllowance(taxOffice.getClaimNICAllowance());
        dto.setClaimEmploymentAllowance(taxOffice.getClaimEmploymentAllowance());
        dto.setChildSupportRef(taxOffice.getChildSupportRef());

        return dto;

    }

    public TaxOffice changeToTaxOffice(TaxOfficeDto taxOfficeDto) {
        TaxOffice taxOffice = new TaxOffice();
        taxOffice.setPayeReference(taxOfficeDto.getPayeReference());
        taxOffice.setAccountsOfficeReference(taxOfficeDto.getAccountsOfficeReference());
        taxOffice.setPaymentMethod(taxOfficeDto.getPaymentMethod());
        taxOffice.setUniqueTaxRef(taxOfficeDto.getUniqueTaxRef());
        taxOffice.setCorporationTaxRef(taxOfficeDto.getCorporationTaxRef());
        taxOffice.setPayrollGivingRef(taxOfficeDto.getPayrollGivingRef());
        taxOffice.setSerQualifiedThisYear(taxOfficeDto.getSerQualifiedThisYear());
        taxOffice.setSerQualifiedLastYear(taxOfficeDto.getSerQualifiedLastYear());
        taxOffice.setNoRtiDueWarnings(taxOfficeDto.getNoRtiDueWarnings());
        taxOffice.setClaimNICAllowance(taxOfficeDto.getClaimNICAllowance());
        taxOffice.setClaimEmploymentAllowance(taxOfficeDto.getClaimEmploymentAllowance());
        taxOffice.setChildSupportRef(taxOfficeDto.getChildSupportRef());

        return taxOffice;


    }
}


