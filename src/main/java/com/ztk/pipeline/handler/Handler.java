package com.ztk.pipeline.handler;

import com.ztk.pipeline.context.HandlerContext;

public interface Handler<T> {

    void handler(HandlerContext context, T t) throws Exception;

}
