package org.openeuler.sbom.manager.model.spdx;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public enum ReferenceCategory {
    SECURITY,

    PACKAGE_MANAGER,

    PROVIDE_MANAGER,

    EXTERNAL_MANAGER,

    PERSISTENT_ID,

    OTHER;

    public static final List<ReferenceCategory> BINARY_TYPE = List.of(
            ReferenceCategory.PACKAGE_MANAGER,
            ReferenceCategory.PROVIDE_MANAGER,
            ReferenceCategory.EXTERNAL_MANAGER
    );

    public static ReferenceCategory findReferenceCategory(String categoryStr) {
        if (StringUtils.isEmpty(categoryStr)) {
            return null;
        }
        for (ReferenceCategory category : ReferenceCategory.values()) {
            if (StringUtils.equalsIgnoreCase(categoryStr, category.name())) {
                return category;
            }
        }
        return null;
    }
}
