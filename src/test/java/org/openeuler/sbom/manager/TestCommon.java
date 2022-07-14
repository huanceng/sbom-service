package org.openeuler.sbom.manager;

import org.openeuler.sbom.manager.model.spdx.SpdxDocument;
import org.openeuler.sbom.manager.model.spdx.SpdxPackage;
import org.openeuler.sbom.manager.model.spdx.SpdxRelationship;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

public class TestCommon {
    public static void assertSpdxDocument(SpdxDocument spdxDocument) {
        assertThat(spdxDocument.getSpdxVersion()).isEqualTo("SPDX-2.2");
        assertThat(spdxDocument.getName()).isEqualTo("Unnamed document");
        assertThat(spdxDocument.getDataLicense()).isEqualTo("CC0-1.0");
        assertThat(spdxDocument.getCreationInfo().creators().size()).isEqualTo(1);
        assertThat(spdxDocument.getCreationInfo().creators().get(0)).isEqualTo("Tool: OSS Review Toolkit - e5b343ff71-dirty");
        assertThat(spdxDocument.getCreationInfo().licenseListVersion()).isEqualTo("3.16");
        assertThat(spdxDocument.getCreationInfo().created()).isEqualTo(Instant.parse("2022-06-27T08:05:09Z"));
        assertThat(spdxDocument.getDocumentNamespace()).isEqualTo("spdx://57eaa8d8-9572-44ff-ace4-d4ac38292265");
        assertThat(spdxDocument.getPackages().size()).isEqualTo(78);
        assertThat(spdxDocument.getRelationships().size()).isEqualTo(77);

        SpdxPackage pkg = spdxDocument.getPackages().get(0);
        assertThat(pkg.getSpdxId()).isEqualTo("SPDXRef-Package-github-abseil-cpp-20210324.2");
        assertThat(pkg.getHomepage()).isEqualTo("https://abseil.io");
        assertThat(pkg.getLicenseDeclared()).isEqualTo("Apache-2.0");
        assertThat(pkg.getName()).isEqualTo("abseil-cpp");
        assertThat(pkg.getVersionInfo()).isEqualTo("20210324.2");

        SpdxRelationship relationship = spdxDocument.getRelationships().get(0);
        assertThat(relationship.spdxElementId()).isEqualTo("SPDXRef-Package-PyPI-asttokens-2.0.5");
        assertThat(relationship.relationshipType().name()).isEqualTo("DEPENDS_ON");
        assertThat(relationship.relatedSpdxElement()).isEqualTo("SPDXRef-Package-PyPI-six-1.16.0");
    }
}
