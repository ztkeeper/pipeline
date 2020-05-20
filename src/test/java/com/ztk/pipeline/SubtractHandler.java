package com.ztk.pipeline;

import com.ztk.pipeline.context.HandlerContext;
import com.ztk.pipeline.handler.HandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubtractHandler extends HandlerAdapter<HandlerModel> {

    protected final Logger log = LoggerFactory.getLogger(SubtractHandler.class);

    @Override
    public void handler(HandlerContext context, HandlerModel msg) throws Exception {
        log.info("{},{}", msg.getParam1(), msg.getParam2());
        HandlerModel result = new HandlerModel();
        result.setParam1(msg.getParam1() - msg.getParam2());
        result.setParam2(2);
        super.handler(context, result);
    }

}
