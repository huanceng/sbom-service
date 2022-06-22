package org.openeuler.sbom.analyzer.parser.handler;

import org.apache.commons.lang3.StringUtils;

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

    public static HandlerFactory getFactory(String handlerType) {
        return factories.stream().filter(it -> StringUtils.equals(it.getHandlerType().getTag(), handlerType))
                .findFirst().orElseThrow(() -> new RuntimeException("invalid handler: [%s]".formatted(handlerType)));
    }
}
