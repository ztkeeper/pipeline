package com.ztk.pipeline.handler;

public interface HandlerInvoker {

    HandlerInvoker fireHandler(Object msg);
}