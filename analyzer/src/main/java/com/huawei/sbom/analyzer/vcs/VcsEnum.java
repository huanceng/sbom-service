package com.huawei.sbom.analyzer.vcs;

public enum VcsEnum {
    GITHUB("github.com"),
    GITEE("gitee.com"),
    GITLAB("gitlab.com");

    private final String vcsHost;

    VcsEnum(String vcsHost) {
        this.vcsHost = vcsHost;
    }

    public String getVcsHost() {
        return this.vcsHost;
    }
}
