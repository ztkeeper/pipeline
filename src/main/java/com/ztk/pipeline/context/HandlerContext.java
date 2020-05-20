package com.ztk.pipeline.context;

import com.ztk.pipeline.handler.Handler;
import com.ztk.pipeline.handler.HandlerInvoker;
import com.ztk.pipeline.pipeline.Pipeline;

public interface HandlerContext extends HandlerInvoker {

    String name();

    Pipeline pipeline();

    Handler handler();

    @Override
    HandlerContext fireHandler(Object msg);
}
