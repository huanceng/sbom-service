package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

public record SpdxCreationInfo(
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String comment,
        Instant created,
        List <String> creators,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) String licenseListVersion
) {}
