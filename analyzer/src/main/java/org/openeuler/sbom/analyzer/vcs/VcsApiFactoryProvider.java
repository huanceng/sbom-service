package org.openeuler.sbom.analyzer.vcs;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class VcsApiFactoryProvider {
    private static final List<VcsApiFactory> factories;

    static {
        ServiceLoader<VcsApiFactory> loader = ServiceLoader.load(VcsApiFactory.class);
        factories = loader.stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
    }

    public static VcsApiFactory getFactory(String vcsHost) {
        return factories.stream().filter(it -> StringUtils.equals(it.getVcs().getVcsHost(), vcsHost))
                .findFirst().orElse(null);
    }
}
