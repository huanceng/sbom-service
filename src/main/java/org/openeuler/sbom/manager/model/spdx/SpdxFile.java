package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpdxFile(
        @JsonProperty("SPDXID") String spdxId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxAnnotation> annotations,
        List<SpdxChecksum> checksums,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        String copyrightText,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> fileContributors,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> fileDependencies,
        @JsonProperty("fileName") String filename,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<FileType> fileTypes,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String licenseComments,
        String licenseConcluded,
        List<String> licenseInfoInFiles,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String noticeText
) {}
