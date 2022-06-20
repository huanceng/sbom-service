package com.huawei.sbom.analyzer.vcs;

import com.huawei.sbom.analyzer.model.RepoInfo;

public interface VcsApi {
    RepoInfo getRepoInfo(String org, String repo);
}
