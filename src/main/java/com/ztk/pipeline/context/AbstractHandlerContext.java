package com.ztk.pipeline.context;

import com.ztk.pipeline.pipeline.DefaultPipeline;
import com.ztk.pipeline.pipeline.Pipeline;
import com.ztk.pipeline.utils.ObjectUtil;

public abstract class AbstractHandlerContext implements HandlerContext {

    public volatile AbstractHandlerContext next;
    public volatile AbstractHandlerContext prev;

    private DefaultPipeline pipeline;
    private String name;

    protected AbstractHandlerContext(DefaultPipeline pipeline, String name) {
        this.name = ObjectUtil.checkNotNull(name, "name");
        this.pipeline = pipeline;
    }

    @Override
    public Pipeline pipeline() {
        return this.pipeline;
    }

    @Override
    public String name() {
        return this.name;
    }

    public static void invokeHandler(final AbstractHandlerContext next, Object msg) {
        next.invokeHandler(msg);
    }

    private void invokeHandler(Object msg) {
        try {
            handler().handler(this, msg);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HandlerContext fireHandler(final Object msg) {
        invokeHandler(findContext(), msg);
        return this;
    }

    private AbstractHandlerContext findContext() {
        AbstractHandlerContext ctx = this;
        ctx = ctx.next;
        return ctx;
    }
}
