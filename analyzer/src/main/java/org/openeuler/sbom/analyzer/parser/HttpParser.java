package org.openeuler.sbom.analyzer.parser;

import org.openeuler.sbom.analyzer.model.HttpSniffData;
import org.openeuler.sbom.analyzer.model.ProcessIdentifier;
import org.openeuler.sbom.analyzer.utils.Mapper;
import org.openeuler.sbom.analyzer.utils.PackageGenerator;
import org.apache.commons.lang3.StringUtils;
import org.ossreviewtoolkit.model.CuratedPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HttpParser implements Parser {

    private static final Logger logger = LoggerFactory.getLogger(HttpParser.class);

    protected final String logPath;

    protected final List<ProcessIdentifier> allProcess;

    public HttpParser(String logPath, List<ProcessIdentifier> allProcess) {
        this.logPath = logPath;
        this.allProcess = allProcess;
    }

    @Override
    public Set<CuratedPackage> parse() {
        logger.info("start to parse HTTP");
        Set<CuratedPackage> packages;
        try(Stream<String> stream = Files.lines(Paths.get(logPath))) {
            packages = stream.map(line -> Mapper.readValue(line.trim(), HttpSniffData.class))
                    .filter(data -> allProcess.contains(new ProcessIdentifier(data.pid(), data.ppid(), data.cmd())))
                    .map(data -> getHostPath(data.data()))
                    .map(this::getPackage)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (IOException e) {
            logger.error("failed to parse HTTP", e);
            throw new RuntimeException(e);
        }
        logger.info("successfully parsed HTTP");
        return packages;
    }

    protected CuratedPackage getPackage(HostPathWrapper wrapper) {
        String host = wrapper.host();
        String path = wrapper.path();
        path = resolveCache(path);
        for (String suffix : Arrays.asList(".tar.gz", ".tar.xz", ".zip", ".tar", ".gz", ".xz")) {
            path = path.replace(suffix, "");
        }

        String dirPattern = "/(.*?)/(.*?)/.*/(\\D*([.\\-_\\da-z]*))/.*";
        String packagePattern = "/(.*?)/(.*?)/.*/(\\D*([.\\-_\\da-z]*))";
        for (String pattern : Arrays.asList(dirPattern, packagePattern)) {
            Matcher matcher = Pattern.compile(pattern).matcher(path);
            if (matcher.matches()) {
                String org = matcher.group(1);
                String repo = matcher.group(2);
                String tag = matcher.group(3);
                String version = matcher.group(4);
                if (Stream.of(org, repo, tag, version).allMatch(StringUtils::isNotEmpty)) {
                    return PackageGenerator.generatePackageFromVcs(host, org, repo, version, "", tag);
                }
            }
        }

        return null;
    }

    private HostPathWrapper getHostPath(String data) {
        String host = "";
        String path = "";
        for (String s : data.trim().split("\r\n")) {
            if (s.startsWith("Host")) {
                host = s.split(" ")[1];
            } else if (Stream.of("GET", "POST", "PUT", "HEAD").anyMatch(s::startsWith)) {
                path = s.split(" ")[1];
            }
        }
        return new HostPathWrapper(host, path);
    }

    private String resolveCache(String path) {
        Matcher matcher = Pattern.compile("(.*)/blazearchive/(.*)\\?.*").matcher(path);
        if (!matcher.matches()) {
            return path;
        }
        return "%s/archive/%s".formatted(matcher.group(1), matcher.group(2));
    }

    protected record HostPathWrapper(String host, String path) {}
}
