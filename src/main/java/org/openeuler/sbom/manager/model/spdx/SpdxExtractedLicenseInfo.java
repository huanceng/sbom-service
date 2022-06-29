package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record SpdxExtractedLicenseInfo(
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        String extractedText,
        String licenseId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String name,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)List<String> seeAlsos
) {}
