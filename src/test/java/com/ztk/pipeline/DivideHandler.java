package com.ztk.pipeline;

import com.ztk.pipeline.context.HandlerContext;
import com.ztk.pipeline.handler.HandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DivideHandler extends HandlerAdapter<HandlerModel> {

    protected final Logger log = LoggerFactory.getLogger(DivideHandler.class);

    @Override
    public void handler(HandlerContext context, HandlerModel msg) throws Exception {
        log.info("{},{}", msg.getParam1(), msg.getParam2());
        Integer result1 = msg.getParam1() / msg.getParam2();
        Integer result2 = msg.getParam1() % msg.getParam2();
        log.info("{} {}", result1, result2);
        System.out.println();
        System.out.println();
        System.out.println();
        super.handler(context, null);
    }

}
