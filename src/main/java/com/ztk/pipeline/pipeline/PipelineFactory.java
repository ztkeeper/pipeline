package com.ztk.pipeline.pipeline;

import com.ztk.pipeline.handler.Handler;

public final class PipelineFactory {

    private PipelineFactory() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        Pipeline pipeline = new DefaultPipeline();

        private Builder() {
        }

        public Builder register(Handler handler) {
            pipeline.addLast(handler);
            return this;
        }

        public Builder register(String name, Handler handler) {
            pipeline.addLast(name, handler);
            return this;
        }

        public Pipeline build() {
            return pipeline;
        }

    }

}
