package com.payroll.uk.payroll_processing.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UkTaxCode {
    // Personal Allowance codes
    L("L", "Basic tax-free Personal Allowance"),
    M("M", "Marriage Allowance (received 10% of partner's Personal Allowance)"),
    N("N", "Marriage Allowance (transferred 10% of Personal Allowance to partner)"),

    // Tax codes without Personal Allowance
    T("T", "Tax code with other calculations needed"),
    BR("BR", "Basic Rate tax (all income taxed at 20%)"),
    D0("D0", "Higher Rate tax (all income taxed at 40%)"),
    D1("D1", "Additional Rate tax (all income taxed at 45%)"),
    NT("NT", "No Tax deducted"),

    // Scottish tax codes
    S("S", "Scottish tax rate applicable"),
    SBR("SBR", "Scottish Basic Rate (all income taxed at 20%)"),
    SD0("SD0", "Scottish Intermediate Rate (all income taxed at 21%)"),
    SD1("SD1", "Scottish Higher Rate (all income taxed at 42%)"),
    SD2("SD2", "Scottish Top Rate (all income taxed at 47%)"),

    // Welsh tax codes (same rates as England and Northern Ireland currently)
    C("C", "Welsh tax rate applicable"),
    CBR("CBR", "Welsh Basic Rate (all income taxed at 20%)"),
    CD0("CD0", "Welsh Higher Rate (all income taxed at 40%)"),
    CD1("CD1", "Welsh Additional Rate (all income taxed at 45%)"),

    // Emergency tax codes
    W1("W1", "Emergency tax code (non-cumulative)"),
    M1("M1", "Emergency tax code (non-cumulative)"),
    X("X", "Emergency tax code (non-cumulative)"),

    // Other special codes
    K("K", "Tax code where deductions exceed allowances"),
    OT("0T", "No Personal Allowance, all income taxed");

    private final String code;
    private final String description;
}