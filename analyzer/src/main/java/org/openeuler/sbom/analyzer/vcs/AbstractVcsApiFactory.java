package org.openeuler.sbom.analyzer.vcs;

public abstract class AbstractVcsApiFactory<T extends VcsApi> implements VcsApiFactory {
    private final VcsEnum vcs;

    protected AbstractVcsApiFactory(VcsEnum vcs) {
        this.vcs = vcs;
    }

    public VcsEnum getVcs() {
        return this.vcs;
    }

    @Override
    public abstract T create();
}
