package org.openeuler.sbom.manager.utils;

import org.apache.commons.lang3.StringUtils;
import org.ossreviewtoolkit.utils.spdx.model.SpdxDocument;

public enum SbomSpecification {
    SPDX_2_2("SPDX", "2.2", SpdxDocument.class),
    CYCLONEDX_1_4("CycloneDX", "1.4", null),
    SWID("SWID", null, null);

    private final String specification;

    private final String version;

    private final Class<?> documentClass;

    SbomSpecification(String specification, String version, Class<?> documentClass) {
        this.specification = specification;
        this.version = version;
        this.documentClass = documentClass;
    }

    public String getSpecification() {
        return specification;
    }

    public String getVersion() {
        return version;
    }

    public static SbomSpecification findSpecification(String specification, String version) {
        for (SbomSpecification spec : SbomSpecification.values()) {
            if (StringUtils.equalsIgnoreCase(specification, spec.getSpecification()) && StringUtils.equalsIgnoreCase(version, spec.getVersion())) {
                return spec;
            }
        }
        return null;
    }

    public Class<?> getDocumentClass() {
        return documentClass;
    }
}