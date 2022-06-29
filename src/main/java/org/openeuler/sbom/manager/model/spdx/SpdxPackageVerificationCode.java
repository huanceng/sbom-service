package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public record SpdxPackageVerificationCode(
        @JsonInclude(JsonInclude.Include.NON_EMPTY)List<String> packageVerificationCodeExcludedFiles,
        String packageVerificationCodeValue
) {}
