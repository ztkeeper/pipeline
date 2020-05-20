package com.ztk.pipeline.context;


import com.ztk.pipeline.handler.Handler;
import com.ztk.pipeline.pipeline.DefaultPipeline;

public final class DefaultHandlerContext extends AbstractHandlerContext {

    private final Handler handler;

    public DefaultHandlerContext(
            DefaultPipeline pipeline, String name, Handler handler) {
        super(pipeline, name);
        this.handler = handler;
    }

    @Override
    public Handler handler() {
        return handler;
    }

}