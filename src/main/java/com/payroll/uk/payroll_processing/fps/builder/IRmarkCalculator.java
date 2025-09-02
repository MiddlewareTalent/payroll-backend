package com.payroll.uk.payroll_processing.fps.builder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class IRmarkCalculator {

    public String generateIRmark(String xmlContent) {
        try {
            // Remove whitespace and normalize if needed before hashing
            String normalized = xmlContent.replaceAll("\\s+", " ");

            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digest = md.digest(normalized.getBytes(StandardCharsets.UTF_8));

            // Convert to Base64 (HMRC expects Base64 IRmark)
            return java.util.Base64.getEncoder().encodeToString(digest);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate IRmark", e);
        }
    }
}
