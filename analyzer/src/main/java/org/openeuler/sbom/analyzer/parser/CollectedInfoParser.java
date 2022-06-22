package org.openeuler.sbom.analyzer.parser;

import org.openeuler.sbom.analyzer.parser.handler.HandlerFactoryProvider;
import org.ossreviewtoolkit.model.CuratedPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class CollectedInfoParser implements Parser {
    private static final Logger logger = LoggerFactory.getLogger(CollectedInfoParser.class);

    private final String logPath;

    public CollectedInfoParser(String logPath) {
        this.logPath = logPath;
    }

    @Override
    public Set<CuratedPackage> parse() {
        logger.info("start to parse collected info");
        Set<CuratedPackage> packages = new TreeSet<>();
        try(Stream<String> stream = Files.lines(Paths.get(logPath))) {
            stream.forEach(line -> HandlerFactoryProvider.getFactories().stream()
                    .map(handlerFactory -> handlerFactory.create().handle(line.trim()))
                    .filter(Objects::nonNull)
                    .forEach(packages::add));
        } catch (IOException e) {
            logger.error("failed to parse collected info", e);
            throw new RuntimeException(e);
        }
        logger.info("successfully parsed collected info");
        return packages;
    }
}
