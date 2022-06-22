package org.openeuler.sbom.analyzer.vcs;

public interface VcsApiFactory {
    VcsEnum getVcs();
    VcsApi create();
}
