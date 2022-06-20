package com.huawei.sbom.analyzer.parser.handler.handlers;

import com.huawei.sbom.analyzer.model.GitSubmoduleData;
import com.huawei.sbom.analyzer.parser.handler.AbstractHandlerFactory;
import com.huawei.sbom.analyzer.parser.handler.Handler;
import com.huawei.sbom.analyzer.parser.handler.HandlerEnum;
import com.huawei.sbom.analyzer.utils.Mapper;
import com.huawei.sbom.analyzer.utils.PackageGenerator;
import org.apache.commons.lang3.StringUtils;
import org.ossreviewtoolkit.model.CuratedPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitSubmoduleHandler implements Handler {
    public static class Factory extends AbstractHandlerFactory<GitSubmoduleHandler> {
        public Factory() {
            super(HandlerEnum.GIT_SUBMODULE);
        }

        @Override
        public GitSubmoduleHandler create() {
            return new GitSubmoduleHandler(this.getHandlerType());
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(GitSubmoduleHandler.class);

    private final HandlerEnum handlerType;

    GitSubmoduleHandler(HandlerEnum handlerType) {
        this.handlerType = handlerType;
    }

    @Override
    public CuratedPackage handle(String recordJson) {
        logger.info("handling git submodule record: '{}'", recordJson);

        GitSubmoduleData data = Mapper.readValue(recordJson, GitSubmoduleData.class);
        if (!StringUtils.equals(data.tag(), handlerType.getTag())) {
            logger.warn("invalid tag for record '{}'", recordJson);
            return null;
        }

        Matcher matcher = Pattern.compile("https://(.*?)/(.*?)/(.*?)\\.git").matcher(data.url().trim());
        if (!matcher.matches()) {
            logger.warn("invalid git url for record '{}'", recordJson);
            return null;
        }
        String host = matcher.group(1);
        String org = matcher.group(2);
        String repo = matcher.group(3);

        String versionString = data.versionString().trim();
        String[] versionStringSplit = versionString.split("/");
        if (versionStringSplit.length > 1) {
            versionString = versionStringSplit[versionStringSplit.length - 1];
        }

        for (String pattern : Arrays.asList("\\D*([.\\-\\da-z]*)-.*-.*", "\\D*([.\\-\\da-z]*)-.*", "\\D*([.\\-\\da-z]*)")) {
            Matcher m = Pattern.compile(pattern).matcher(versionString);
            if (m.matches()) {
                String version = m.group(1);
                return PackageGenerator.generatePackageFromVcs(host, org, repo, version, data.commitId().trim(), "");
            }
        }

        logger.warn("invalid record '{}'", recordJson);
        return null;
    }
}
