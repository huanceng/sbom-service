package org.openeuler.sbom.manager.utils;

import java.util.Map;

public enum SbomFormat {
    JSON("json"),
    YAML("yaml"),
    XML("xml"),
    RDF("rdf");

    private final String fileExtName;

    SbomFormat(String fileExtName) {
        this.fileExtName = fileExtName;
    }

    public String getFileExtName() {
        return fileExtName;
    }

    public static final Map<String, SbomFormat> EXT_TO_FORMAT = Map.of(
            "json", SbomFormat.JSON,
            "yml", SbomFormat.YAML,
            "yaml", SbomFormat.YAML,
            "xml", SbomFormat.XML,
            "rdf", SbomFormat.RDF,
            "rdf.xml", SbomFormat.RDF
    );
}
