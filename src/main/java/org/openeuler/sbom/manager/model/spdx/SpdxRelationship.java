package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;

public record SpdxRelationship(
        String spdxElementId,
        RelationshipType relationshipType,
        String relatedSpdxElement,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment
) {}
