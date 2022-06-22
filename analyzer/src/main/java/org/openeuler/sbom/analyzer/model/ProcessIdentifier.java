package org.openeuler.sbom.analyzer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ProcessIdentifier(@JsonProperty(required = true) Integer pid,
                                @JsonProperty(required = true) Integer ppid,
                                @JsonProperty(required = true) String cmd) implements Serializable {}
