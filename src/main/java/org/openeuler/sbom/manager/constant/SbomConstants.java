package org.openeuler.sbom.manager.constant;

import java.util.Arrays;
import java.util.List;

public class SbomConstants {
    public static final String READER_NAME = "reader";
    public static final String WRITER_NAME = "writer";

    public static final String SPDX_NAME = "SPDX";
    public static final String CYCLONEDX_NAME = "CycloneDX";
    public static final String SWID_NAME = "SWID";

    public static final int MAX_QUERY_LINE = 15;

    public static final List<String> ALLOW_ORIGINS= Arrays.asList("http://localhost:8080","http://127.0.0.1:8080");

}
