package com.huawei.sbom.analyzer.parser.handler;

public interface HandlerFactory {
    HandlerEnum getHandlerType();
    Handler create();
}
