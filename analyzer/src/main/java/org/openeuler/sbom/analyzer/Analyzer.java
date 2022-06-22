package org.openeuler.sbom.analyzer;

import org.openeuler.sbom.analyzer.model.ProcessIdentifier;
import org.openeuler.sbom.analyzer.parser.CollectedInfoParser;
import org.openeuler.sbom.analyzer.parser.Http2Parser;
import org.openeuler.sbom.analyzer.parser.HttpParser;
import org.openeuler.sbom.analyzer.parser.ProcessParser;
import org.openeuler.sbom.analyzer.utils.FileUtil;
import org.openeuler.sbom.analyzer.utils.Mapper;
import org.apache.commons.io.FileUtils;
import org.ossreviewtoolkit.model.AnalyzerResult;
import org.ossreviewtoolkit.model.AnalyzerRun;
import org.ossreviewtoolkit.model.CuratedPackage;
import org.ossreviewtoolkit.model.DependencyGraph;
import org.ossreviewtoolkit.model.DependencyGraphNode;
import org.ossreviewtoolkit.model.Identifier;
import org.ossreviewtoolkit.model.OrtResult;
import org.ossreviewtoolkit.model.PackageLinkage;
import org.ossreviewtoolkit.model.Project;
import org.ossreviewtoolkit.model.Repository;
import org.ossreviewtoolkit.model.RootDependencyIndex;
import org.ossreviewtoolkit.model.VcsInfo;
import org.ossreviewtoolkit.model.config.AnalyzerConfiguration;
import org.ossreviewtoolkit.utils.ort.Environment;
import org.ossreviewtoolkit.utils.ort.ProcessedDeclaredLicense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Analyzer {
    private static final Logger logger = LoggerFactory.getLogger(Analyzer.class);

    private final String tarFile;

    private final String outputDir;

    private static final String EXECSNOOP_LOG = "execsnoop.log";

    private static final String HTTP_SNIFF_LOG = "sslsniff.log";

    private static final String HTTP2_SNIFF_LOG = "h2sniff.log";

    private static final String COLLECTED_INFO_LOG = "locally_collected_info.log";

    private static final String RESULT_FILE = "trace_result.json";

    private static final String PROJECT_MANAGER_TYPE = "Tracer";

    private static final String PROJECT_NAME = "Git";

    private static final String PROJECT_SCOPE = "compile";

    private static final List<String> RESERVED_FILES = new ArrayList<>(List.of("requirements.txt"));

    public Analyzer(String tarFile, String outputDir) {
        this.tarFile = tarFile;
        this.outputDir = outputDir;
    }

    public void analyze() {
        Path workspacePath = null;
        try {
            logger.info("start to analyze {}", tarFile);

            Instant StartTime = Instant.now();

            Path tarPath = Path.of(tarFile);
            String taskId = tarPath.getFileName().toString().split("_")[0];

            workspacePath = Files.createTempDirectory(PROJECT_MANAGER_TYPE + "-" + taskId + "-");
            String workspace = workspacePath.toString();
            FileUtil.extractTarGzipArchive(tarFile, workspace);

            List<ProcessIdentifier> allProcess = new ProcessParser(Paths.get(workspace, EXECSNOOP_LOG).toString(), taskId).getAllProcess();
            Set<CuratedPackage> httpPackages = new HttpParser(Paths.get(workspace, HTTP_SNIFF_LOG).toString(), allProcess).parse();
            Set<CuratedPackage> http2Packages = new Http2Parser(Paths.get(workspace, HTTP2_SNIFF_LOG).toString(), allProcess).parse();
            Set<CuratedPackage> otherPackages = new CollectedInfoParser(Paths.get(workspace, COLLECTED_INFO_LOG).toString()).parse();
            TreeSet<CuratedPackage> packages = new TreeSet<>();
            for (Set<CuratedPackage> curatedPackages : Arrays.asList(httpPackages, http2Packages, otherPackages)) {
                packages.addAll(curatedPackages);
            }

            Identifier identifier = new Identifier(PROJECT_MANAGER_TYPE, "", PROJECT_NAME, "");
            Project project = new Project(identifier, "", "", new TreeSet<>(), new TreeSet<>(),
                    ProcessedDeclaredLicense.EMPTY, VcsInfo.EMPTY, VcsInfo.EMPTY, "", null,
                    new TreeSet<>(Set.of(PROJECT_SCOPE)));
            Map<String, DependencyGraph> dependencyGraphs = generateDependencyGraphs(packages, project);
            AnalyzerResult analyzerResult = new AnalyzerResult(new TreeSet<>(Set.of(project)), packages, new TreeMap<>(), dependencyGraphs);
            AnalyzerRun analyzerRun = new AnalyzerRun(StartTime, Instant.now(), new Environment(), new AnalyzerConfiguration(), analyzerResult);
            OrtResult ortResult = new OrtResult(Repository.EMPTY, analyzerRun, null, null, null, new TreeMap<>());

            FileUtil.ensureDirExists(outputDir);
            Path outputPath = Path.of(outputDir, RESULT_FILE);
            Mapper.jsonMapper.findAndRegisterModules().writerWithDefaultPrettyPrinter().writeValue(outputPath.toFile(), ortResult);

            for (String f : RESERVED_FILES) {
                FileUtils.copyFileToDirectory(Paths.get(workspace, f).toFile(), Path.of(outputDir).toFile());
            }

            FileUtils.deleteQuietly(tarPath.toFile());
            logger.info("successfully analyzed, result is written to '{}'", outputPath);
        } catch (IOException e) {
            logger.error("failed to analyze", e);
            throw new RuntimeException(e);
        } finally {
            if (workspacePath != null) {
                FileUtils.deleteQuietly(workspacePath.toFile());
            }
        }
    }

    private Map<String, DependencyGraph> generateDependencyGraphs(Set<CuratedPackage> packages, Project project) {
        List<Identifier> packageIds = packages.stream().map(p -> p.getPkg().getId()).toList();
        List<DependencyGraphNode> nodes = IntStream.range(0, packages.size())
                .mapToObj(i -> new DependencyGraphNode(i, 0, PackageLinkage.DYNAMIC, new ArrayList<>()))
                .collect(Collectors.toList());
        Map<String, List<RootDependencyIndex>> scopes = new HashMap<>();
        scopes.put(":%s::%s".formatted(project.getId().getName(), PROJECT_SCOPE),
                IntStream.range(0, packages.size())
                        .mapToObj(i -> new RootDependencyIndex(i, 0))
                        .collect(Collectors.toList()));
        DependencyGraph dependencyGraph = new DependencyGraph(packageIds, new TreeSet<>(), scopes, nodes, new ArrayList<>());
        Map<String, DependencyGraph> dependencyGraphs = new HashMap<>();
        dependencyGraphs.put(project.getId().getType(), dependencyGraph);
        return dependencyGraphs;
    }
}
