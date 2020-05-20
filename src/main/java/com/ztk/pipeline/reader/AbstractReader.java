package com.ztk.pipeline.reader;

import com.ztk.pipeline.pipeline.Pipeline;

public abstract class AbstractReader implements Reader {

    private final Pipeline pipeline;

    public AbstractReader(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public void submit(Object msg) {
        pipeline.fireHandler(msg);
    }

}
