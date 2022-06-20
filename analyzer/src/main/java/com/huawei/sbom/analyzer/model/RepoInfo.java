package com.huawei.sbom.analyzer.model;

import java.util.SortedSet;

public record RepoInfo(SortedSet<String> authors, SortedSet<String> licenses, String description, String homepageUrl,
                       String repoUrl) {}
