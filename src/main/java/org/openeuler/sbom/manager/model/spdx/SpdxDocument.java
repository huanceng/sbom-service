package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpdxDocument(
        @JsonProperty("SPDXID") String spdxId,
        String spdxVersion,
        SpdxCreationInfo creationInfo,
        String name,
        String dataLicense,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxExternalDocumentReference> externalDocumentRefs,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxExtractedLicenseInfo> hasExtractedLicensingInfos,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxAnnotation> annotations,
        String documentNamespace,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<String> documentDescribes,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxPackage> packages,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxFile> files,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxSnippet> snippets,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) List<SpdxRelationship> relationships
        ) {}
