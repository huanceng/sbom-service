package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;

public record SpdxExternalReference(
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        ReferenceCategory referenceCategory,
        ReferenceType referenceType,
        String referenceLocator
) {}
