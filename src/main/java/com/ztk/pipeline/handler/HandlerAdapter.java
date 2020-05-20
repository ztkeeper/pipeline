package com.ztk.pipeline.handler;

import com.ztk.pipeline.context.HandlerContext;

public abstract class HandlerAdapter<T> implements Handler<T> {

    @Override
    public void handler(HandlerContext context, T msg) throws Exception {
        context.fireHandler(msg);
    }
}
