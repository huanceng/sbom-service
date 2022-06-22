package org.openeuler.sbom.analyzer.parser;

import org.openeuler.sbom.analyzer.model.ProcessData;
import org.openeuler.sbom.analyzer.model.ProcessIdentifier;
import org.openeuler.sbom.analyzer.utils.Mapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ProcessParser {
    private static final Logger logger = LoggerFactory.getLogger(ProcessParser.class);
    private final String logPath;

    private final String taskId;

    public ProcessParser(String logPath, String taskId) {
        this.logPath = logPath;
        this.taskId = taskId;
    }

    public List<ProcessIdentifier> getAllProcess() {
        logger.info("start to get all processes");
        List<ProcessIdentifier> allProcess = new ArrayList<>();
        var wrapper = new Object() {int mainPid = -1;};
        try(Stream<String> stream = Files.lines(Paths.get(logPath))) {
            stream.forEach(line -> {
                ProcessData data = Mapper.readValue(line.trim(), ProcessData.class);
                if (StringUtils.contains(data.fullCmd(), taskId + "_")) {
                    wrapper.mainPid = data.pid();
                }
                if (StringUtils.contains(data.fullCmd(), taskId + "_") ||
                        data.ancestorPids().contains(wrapper.mainPid)) {
                    allProcess.add(new ProcessIdentifier(data.pid(), data.ppid(), data.cmd()));
                }
            });
        } catch (IOException e) {
            logger.error("failed to get all processes", e);
            throw new RuntimeException(e);
        }
        logger.info("successfully got all processes");
        return allProcess;
    }
}
