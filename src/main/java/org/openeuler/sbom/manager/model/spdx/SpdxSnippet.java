package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpdxSnippet(
        @JsonProperty("SPDXID") String spdxId,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)List<SpdxAnnotation> annotations,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        String copyrightText,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String licenseComments,
        String licenseConcluded,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> licenseInfoInSnippets,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String name,
        String snippetFromFile
) {}
