package org.openeuler.sbom.analyzer.vcs;

import org.openeuler.sbom.analyzer.model.RepoInfo;

public interface VcsApi {
    RepoInfo getRepoInfo(String org, String repo);
}
