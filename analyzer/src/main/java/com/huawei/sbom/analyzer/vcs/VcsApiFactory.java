package com.huawei.sbom.analyzer.vcs;

public interface VcsApiFactory {
    VcsEnum getVcs();
    VcsApi create();
}
