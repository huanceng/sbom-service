package org.openeuler.sbom.manager.model.spdx;

public record SpdxExternalDocumentReference(
        String externalDocumentId,
        String spdxDocument,
        SpdxChecksum checksum
) {}
