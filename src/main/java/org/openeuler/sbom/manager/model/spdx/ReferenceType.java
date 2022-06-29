package org.openeuler.sbom.manager.model.spdx;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReferenceType {
    CPE_22_TYPE("cpe22Type"),

    CPE_23_TYPE("cpe23Type"),

    BOWER("bower"),

    MAVEN_CENTRAL("maven-central"),

    NPM("npm"),

    NUGET("nuget"),

    PURL("purl"),

    SOFTWARE_HERITAGE("swh");

    private final String name;

    ReferenceType(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }
}
