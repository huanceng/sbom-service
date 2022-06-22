package org.openeuler.sbom.analyzer.parser.handler;

public interface HandlerFactory {
    HandlerEnum getHandlerType();
    Handler create();
}
