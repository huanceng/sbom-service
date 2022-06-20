package com.huawei.sbom.analyzer.utils;

import com.huawei.sbom.analyzer.model.RepoInfo;
import com.huawei.sbom.analyzer.vcs.VcsApiFactory;
import com.huawei.sbom.analyzer.vcs.VcsApiFactoryProvider;
import org.apache.commons.lang3.StringUtils;
import org.ossreviewtoolkit.model.CuratedPackage;
import org.ossreviewtoolkit.model.Identifier;
import org.ossreviewtoolkit.model.Package;
import org.ossreviewtoolkit.model.RemoteArtifact;
import org.ossreviewtoolkit.model.VcsInfo;
import org.ossreviewtoolkit.model.VcsType;
import org.ossreviewtoolkit.model.utils.ExtensionsKt;
import org.ossreviewtoolkit.utils.ort.ProcessedDeclaredLicense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class PackageGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PackageGenerator.class);

    public static CuratedPackage generatePackageFromVcs(String host, String org, String repo, String version,
                                                        String commitId, String tag) {
        logger.info("start to generate package from vcs for [host: '{}', org: '{}', repo: '{}']", host, org, repo);

        VcsApiFactory factory = VcsApiFactoryProvider.getFactory(host);
        if (factory == null) {
            logger.warn("invalid vcs: '{}'", host);
            return null;
        }

        RepoInfo repoInfo = factory.create().getRepoInfo(org, repo);
        String revision = StringUtils.isEmpty(tag) ? commitId : tag;
        Identifier identifier = new Identifier(factory.getVcs().toString().toLowerCase(), org, repo, version);
        VcsInfo vcsInfo = new VcsInfo(VcsType.Companion.getGIT(), repoInfo.repoUrl(), revision, "");
        Package vcsPackage = new Package(identifier, ExtensionsKt.toPurl(identifier), "", repoInfo.authors(),
                repoInfo.licenses(), ProcessedDeclaredLicense.EMPTY, null, repoInfo.description(),
                repoInfo.homepageUrl(), RemoteArtifact.EMPTY, RemoteArtifact.EMPTY, vcsInfo, vcsInfo.normalize(),
                false, false);
        logger.info("successfully generated package from vcs for [host: '{}', org: '{}', repo: '{}']", host, org, repo);
        return new CuratedPackage(vcsPackage, new ArrayList<>());
    }
}
