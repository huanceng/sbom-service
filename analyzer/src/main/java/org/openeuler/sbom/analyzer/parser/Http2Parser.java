package org.openeuler.sbom.analyzer.parser;

import org.openeuler.sbom.analyzer.model.Http2SniffData;
import org.openeuler.sbom.analyzer.model.ProcessIdentifier;
import org.openeuler.sbom.analyzer.utils.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.ossreviewtoolkit.model.CuratedPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Http2Parser extends HttpParser {

    private static final Logger logger = LoggerFactory.getLogger(Http2Parser.class);

    public Http2Parser(String logPath, List<ProcessIdentifier> allProcess) {
        super(logPath, allProcess);
    }

    @Override
    public Set<CuratedPackage> parse() {
        logger.info("start to parse HTTP/2");
        Set<CuratedPackage> packages;
        try(Stream<String> stream = Files.lines(Paths.get(logPath))) {
            packages = stream.map(line -> Mapper.readValue(line.trim(), Http2SniffData.class))
                    .filter(data -> allProcess.contains(new ProcessIdentifier(data.pid(), data.ppid(), data.cmd())))
                    .map(data -> getHostPath(data.data()))
                    .map(this::getPackage)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(TreeSet::new));
        } catch (IOException e) {
            logger.error("failed to parse HTTP/2", e);
            throw new RuntimeException(e);
        }
        logger.info("successfully parsed HTTP/2");
        return packages;
    }

    protected HostPathWrapper getHostPath(List<List<String>> data) {
        String host = "";
        String path = "";

        for (List<String> datum : data) {
            if (StringUtils.equals(datum.get(0), ":authority")) {
                host = datum.get(1);
            } else if (StringUtils.equals(datum.get(0), ":path")) {
                path = datum.get(1);
            }
        }
        return new HostPathWrapper(host, path);
    }
}
