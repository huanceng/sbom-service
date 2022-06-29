package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpdxPackage(
        @JsonProperty("SPDXID") String spdxId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxAnnotation> annotations,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> attributionTexts,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxChecksum> checksums,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        String copyrightText,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String description,
        String downloadLocation,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxExternalReference> externalRefs,
        Boolean filesAnalyzed,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> hasFiles,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String homepage,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String licenseComments,
        String licenseConcluded,
        String licenseDeclared,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> licenseInfoFromFiles,
        String name,
        @JsonInclude(JsonInclude.Include.NON_NULL) String originator,
        @JsonProperty("packageFileName") @JsonInclude(JsonInclude.Include.NON_EMPTY) String packageFilename,
        @JsonInclude(JsonInclude.Include.NON_NULL) SpdxPackageVerificationCode packageVerificationCode,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String sourceInfo,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String summary,
        @JsonInclude(JsonInclude.Include.NON_NULL) String supplier,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String versionInfo
) {}
