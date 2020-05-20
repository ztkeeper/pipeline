package com.ztk.pipeline.pipeline;

import com.ztk.pipeline.context.AbstractHandlerContext;
import com.ztk.pipeline.context.DefaultHandlerContext;
import com.ztk.pipeline.context.HandlerContext;
import com.ztk.pipeline.handler.Handler;
import com.ztk.pipeline.utils.StringUtil;

import java.util.Map;
import java.util.WeakHashMap;

public class DefaultPipeline implements Pipeline {

    private static final String HEAD_NAME = generateName0(HeadContext.class);
    private static final String TAIL_NAME = generateName0(TailContext.class);

    private final ThreadLocal<Map<Class<?>, String>> nameCaches = new ThreadLocal<>();

    final AbstractHandlerContext head;
    final AbstractHandlerContext tail;

    public DefaultPipeline() {
        this.tail = new TailContext(this);
        this.head = new HeadContext(this);
        this.head.next = this.tail;
        this.tail.prev = this.head;
        this.nameCaches.set(new WeakHashMap<>());
    }

    @Override
    public Pipeline addLast(Handler... handlers) {
        for (Handler h : handlers) {
            if (h == null) {
                break;
            }
            addLast(null, h);
        }

        return this;
    }

    @Override
    public Pipeline addLast(String name, Handler handler) {
        final AbstractHandlerContext newCtx;
        synchronized (this) {
            newCtx = newContext(filterName(name, handler), handler);
            addLast0(newCtx);
        }
        return this;
    }

    @Override
    public Handler get(String name) {
        HandlerContext ctx = context(name);
        if (ctx == null) {
            return null;
        } else {
            return ctx.handler();
        }
    }

    @Override
    public HandlerContext context(String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        return context0(name);
    }

    @Override
    public Pipeline fireHandler(Object msg) {
        AbstractHandlerContext.invokeHandler(head, msg);
        return this;
    }

    private static String generateName0(Class<?> handlerType) {
        return StringUtil.simpleClassName(handlerType) + "#0";
    }

    private AbstractHandlerContext newContext(String name, Handler handler) {
        return new DefaultHandlerContext(this, name, handler);
    }

    private String filterName(String name, Handler handler) {
        if (name == null) {
            return generateName(handler);
        }
        checkDuplicateName(name);
        return name;
    }

    private String generateName(Handler handler) {
        Map<Class<?>, String> cache = nameCaches.get();
        Class<?> handlerType = handler.getClass();
        String name = cache.get(handlerType);
        if (name == null) {
            name = generateName0(handlerType);
            cache.put(handlerType, name);
        }
        if (context0(name) != null) {
            String baseName = name.substring(0, name.length() - 1);
            for (int i = 1; ; i++) {
                String newName = baseName + i;
                if (context0(newName) == null) {
                    name = newName;
                    break;
                }
            }
        }
        return name;
    }

    private void checkDuplicateName(String name) {
        if (context0(name) != null) {
            throw new IllegalArgumentException("Duplicate handler name: " + name);
        }
    }

    private AbstractHandlerContext context0(String name) {
        AbstractHandlerContext context = head.next;
        while (context != tail) {
            if (context.name().equals(name)) {
                return context;
            }
            context = context.next;
        }
        return null;
    }

    private void addLast0(AbstractHandlerContext newCtx) {
        AbstractHandlerContext prev = tail.prev;
        newCtx.prev = prev;
        newCtx.next = tail;
        prev.next = newCtx;
        tail.prev = newCtx;
    }


    final class HeadContext extends AbstractHandlerContext implements Handler {

        HeadContext(DefaultPipeline pipeline) {
            super(pipeline, DefaultPipeline.HEAD_NAME);
        }

        @Override
        public Handler handler() {
            return this;
        }

        @Override
        public void handler(HandlerContext context, Object msg) throws Exception {
            context.fireHandler(msg);
        }
    }

    final class TailContext extends AbstractHandlerContext implements Handler {

        TailContext(DefaultPipeline pipeline) {
            super(pipeline, DefaultPipeline.TAIL_NAME);
        }

        @Override
        public Handler handler() {
            return this;
        }

        @Override
        public void handler(HandlerContext context, Object msg) throws Exception {
        }
    }
}
