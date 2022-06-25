package org.openeuler.sbom.analyzer.parser.handler;

import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class HandlerFactoryProvider {
    private static final List<HandlerFactory> factories;

    static {
        ServiceLoader<HandlerFactory> loader = ServiceLoader.load(HandlerFactory.class);
        factories = loader.stream().map(ServiceLoader.Provider::get).collect(Collectors.toList());
    }

    public static List<HandlerFactory> getFactories() {
        return factories;
    }
}
