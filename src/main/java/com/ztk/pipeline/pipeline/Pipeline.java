package com.ztk.pipeline.pipeline;

import com.ztk.pipeline.context.HandlerContext;
import com.ztk.pipeline.handler.Handler;
import com.ztk.pipeline.handler.HandlerInvoker;

public interface Pipeline extends HandlerInvoker {

    Pipeline addLast(Handler... handlers);

    Pipeline addLast(String name, Handler handler);

    Handler get(String name);

    HandlerContext context(String name);

    @Override
    Pipeline fireHandler(Object msg);
}
