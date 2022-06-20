package com.huawei.sbom.analyzer.parser.handler;

import org.ossreviewtoolkit.model.CuratedPackage;

public interface Handler {
    CuratedPackage handle(String recordJson);
}
