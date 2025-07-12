package com.payroll.uk.payroll_processing.entity;

/**
 * Represents both Employee and Employer NIC contribution bands for 2025–2026.
 */
public enum NICBand {

    // ----------------------------
    // EMPLOYEE BANDS
    // ----------------------------

    /**
     * EMPLOYEE_BELOW_LEL:
     * Full Form: Below Lower Earnings Limit
     * Range: £0 to £6,500
     * Meaning: No NIC is due. However, if at least £123/week is earned, it counts towards state pension.
     */
    EMPLOYEE_BELOW_LEL,

    /**
     * EMPLOYEE_LEL_TO_PT:
     * Full Form: Lower Earnings Limit to Primary Threshold
     * Range: £6,500.01 to £12,570
     * Meaning: No NIC payable, but the earnings contribute to National Insurance record (qualifying year).
     */
    EMPLOYEE_LEL_TO_PT,

    /**
     * EMPLOYEE_PT_TO_UEL:
     * Full Form: Primary Threshold to Upper Earnings Limit
     * Range: £12,570.01 to £50,270
     * Meaning: Standard 8% employee NIC is charged on this band.
     */
    EMPLOYEE_PT_TO_UEL,

    /**
     * EMPLOYEE_ABOVE_UEL:
     * Full Form: Above Upper Earnings Limit
     * Range: Over £50,270
     * Meaning: Employee pays 2% NIC on income above this level.
     */
    EMPLOYEE_ABOVE_UEL,

    // ----------------------------
    // EMPLOYER BANDS
    // ----------------------------

    /**
     * EMPLOYER_BELOW_ST:
     * Full Form: Below Secondary Threshold
     * Range: £0 to £5,000
     * Meaning: No employer NIC is due below this level.
     */
    EMPLOYER_BELOW_ST,
    EMPLOYER_BELOW_LEL_2024_2025,

    /**
     * EMPLOYER_ST_TO_LEL:
     * Full Form: Secondary Threshold to Lower Earnings Limit
     * Range: £5,000.01 to £6,500
     * Meaning: May be charged at 15% or 0% depending on NIC category letter.
     */
    EMPLOYER_ST_TO_LEL,

    /**
     * EMPLOYER_LEL_TO_UST:
     * Full Form: Lower Earnings Limit to Upper Secondary Threshold
     * Range: £6,500.01 to £25,000 (Freeport/IZ) or £50,270 (standard)
     * Meaning: Main NIC band where employers pay 0% or 15% depending on category.
     */
    EMPLOYER_LEL_TO_UST,

    /**
     * EMPLOYER_UST_TO_UEL:
     * Full Form: Upper Secondary Threshold to Upper Earnings Limit
     * Range: Only applies when UST < UEL (e.g., Freeport UST is £25,000)
     * Meaning: 15% NIC resumes here for some employer categories.
     */
    EMPLOYER_UST_TO_UEL,

    /**
     * EMPLOYER_ABOVE_UEL_OR_UST:
     * Full Form: Above Upper Earnings Limit or Upper Secondary Threshold
     * Range: Over £50,270 (or over UST, depending on category)
     * Meaning: 15% employer NIC applies universally above this threshold.
     */
    EMPLOYER_ABOVE_UEL;




}
