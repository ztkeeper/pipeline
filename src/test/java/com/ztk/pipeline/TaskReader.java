package com.ztk.pipeline;

import com.ztk.pipeline.pipeline.Pipeline;
import com.ztk.pipeline.reader.ExecutorBasedTaskReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public final class TaskReader extends ExecutorBasedTaskReader<TaskReader> {

    private final Logger log = LoggerFactory.getLogger(TaskReader.class);

    private final AtomicInteger atomic = new AtomicInteger(0);

    public TaskReader(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public void handle() {

        super.submit(new HandlerModel()
                .setParam1(atomic.incrementAndGet())
                .setParam2(atomic.incrementAndGet()));
    }

    @Override
    public void init(){
    }
}
