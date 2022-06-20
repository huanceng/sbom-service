package com.huawei.sbom.analyzer.parser.handler;

public enum HandlerEnum {
    GIT_SUBMODULE("git_submodule");
    private final String tag;

    HandlerEnum(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return this.tag;
    }
}
