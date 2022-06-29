package org.openeuler.sbom.manager.model.spdx;

import java.time.Instant;

public record SpdxAnnotation(
        Instant annotationDate,
        AnnotationType annotationType,
        String annotator,
        String comment
) {}
