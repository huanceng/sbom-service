package org.openeuler.sbom.analyzer.parser.handler;

public abstract class AbstractHandlerFactory<T extends Handler> implements HandlerFactory {
    private final HandlerEnum handlerType;

    protected AbstractHandlerFactory(HandlerEnum handlerType) {
        this.handlerType = handlerType;
    }

    public HandlerEnum getHandlerType() {
        return this.handlerType;
    }

    @Override
    public abstract T create();
}
