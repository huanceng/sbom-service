package org.openeuler.sbom.manager.utils;

import org.apache.commons.lang3.StringUtils;
import org.openeuler.sbom.manager.constant.SbomConstants;
import org.openeuler.sbom.manager.model.spdx.SpdxDocument;

public enum SbomSpecification {
    SPDX_2_2(SbomConstants.SPDX_NAME, "2.2", SpdxDocument.class),
    CYCLONEDX_1_4(SbomConstants.CYCLONEDX_NAME, "1.4", null),
    SWID(SbomConstants.SWID_NAME, null, null);

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
