package org.openeuler.sbom.manager.model.spdx;

public record SpdxChecksum(
        Algorithm algorithm,
        String checksumValue
) {}
