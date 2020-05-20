package com.ztk.pipeline;

import com.ztk.pipeline.pipeline.DefaultPipeline;
import com.ztk.pipeline.pipeline.Pipeline;
import com.ztk.pipeline.reader.ExecutorBasedTaskReader;
import com.ztk.pipeline.reader.ReadTaskProperties;
import com.ztk.pipeline.utils.thread.ThreadPool;

public class Test {

    public static void main(String[] args) {

        // 手动提交
        ManualReader manualReader = new ManualReader(getPieline());
        manualReader.submit(new HandlerModel().setParam1(100).setParam2(200));


        // 定时调度；TaskReader.handle() 用于接受数据 -> 提交数据
        ReadTaskProperties readTaskProperties = new ReadTaskProperties()
                .setInitialDelay(1000)
                .setPeriod(1000);

        ExecutorBasedTaskReader reader = new TaskReader(getPieline())
                .properties(readTaskProperties);

        reader.start();

        ThreadPool.localSleep(10000);

        reader.stop();
    }

    private static Pipeline getPieline() {
        Pipeline pipeline = new DefaultPipeline();
        AddHandler addHandler = new AddHandler();
        SubtractHandler subtractHandler = new SubtractHandler();
        MultiplyHandler multiplyHandler = new MultiplyHandler();
        DivideHandler divideHandler = new DivideHandler();
        pipeline.addLast(addHandler)
                .addLast(subtractHandler)
                .addLast(multiplyHandler)
                .addLast(divideHandler);
        return pipeline;
    }

}
