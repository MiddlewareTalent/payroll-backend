package com.payroll.uk.payroll_processing.dto.mapper.employerdtomapper;

import com.payroll.uk.payroll_processing.dto.employerdto.TermsDto;
import com.payroll.uk.payroll_processing.entity.employer.Terms;
import org.springframework.stereotype.Component;

@Component
public class TermsDtoMapper {
    public TermsDto mapToDto(TermsDto termsDto) {
        TermsDto dto = new TermsDto();
        dto.setHoursWorkedPerWeek(termsDto.getHoursWorkedPerWeek());
        dto.setIsPaidOvertime(termsDto.getIsPaidOvertime());
        dto.setWeeksNoticeRequired(termsDto.getWeeksNoticeRequired());
        dto.setDaysSicknessOnFullPay(termsDto.getDaysSicknessOnFullPay());
        dto.setMaleRetirementAge(termsDto.getMaleRetirementAge());
        dto.setFemaleRetirementAge(termsDto.getFemaleRetirementAge());
        dto.setMayJoinPensionScheme(termsDto.getMayJoinPensionScheme());
        dto.setDaysHolidayPerYear(termsDto.getDaysHolidayPerYear());
        dto.setMaxDaysToCarryOver(termsDto.getMaxDaysToCarryOver());

        return dto;
    }
    public Terms changeToTerms(TermsDto termsDto) {
        Terms terms = new Terms();
        terms.setHoursWorkedPerWeek(termsDto.getHoursWorkedPerWeek());
        terms.setIsPaidOvertime(termsDto.getIsPaidOvertime());
        terms.setWeeksNoticeRequired(termsDto.getWeeksNoticeRequired());
        terms.setDaysSicknessOnFullPay(termsDto.getDaysSicknessOnFullPay());
        terms.setMaleRetirementAge(termsDto.getMaleRetirementAge());
        terms.setFemaleRetirementAge(termsDto.getFemaleRetirementAge());
        terms.setMayJoinPensionScheme(termsDto.getMayJoinPensionScheme());
        terms.setDaysHolidayPerYear(termsDto.getDaysHolidayPerYear());
        terms.setMaxDaysToCarryOver(termsDto.getMaxDaysToCarryOver());

        return terms;
    }

}

