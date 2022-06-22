package org.openeuler.sbom.analyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProcessData(@JsonProperty(required = true) Integer pid,
                          @JsonProperty(required = true) Integer ppid,
                          @JsonProperty(required = true) String cmd,
                          @JsonProperty(value = "full_cmd", required = true) String fullCmd,
                          @JsonProperty(value = "ancestor_pids", required = true) List<Integer> ancestorPids,
                          String cwd) implements Serializable {}
