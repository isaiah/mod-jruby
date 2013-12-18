package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.anno.JRubyModule;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by isaiah on 12/15/13.
 */
@JRubyModule(name="EventBus")
public class RubyEventBus {
    private static Map<String, Handler<? extends Message>> handlerMap = new HashMap<>();
    private static EventBus eb = JRubyVerticleFactory.vertx.eventBus();

    public static RubyModule createEventBusModule(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyModule klazz = vertxModule.defineModuleUnder("EventBus");
        klazz.defineAnnotatedMethods(RubyEventBus.class);
        return klazz;
    }

    @JRubyMethod(name="send", required=2, optional=1, module=true)
    public static IRubyObject send(ThreadContext context, IRubyObject recv, IRubyObject[] args) {
        IRubyObject msg = args[1];
        String addr = args[0].asJavaString();
        if (msg instanceof RubyHash) {
            //TODO
        } else if (msg instanceof RubyBuffer) {
            eb.send(addr, ((RubyBuffer)msg).getBuffer());
        } else if (msg instanceof RubyFloat) {
            eb.send(addr, RubyNumeric.num2dbl(msg));
        } else if (msg instanceof RubyFixnum) {
            eb.send(addr, RubyNumeric.num2long(msg));
        } else
            throw context.runtime.newArgumentError("Unknown type of message " + msg.getType());
        return context.runtime.getNil();
    }

    @JRubyMethod(name="publish", module=true)
    public static IRubyObject publish(ThreadContext context, IRubyObject self, IRubyObject address, IRubyObject msg) {
        String addr = address.asJavaString();
        if (msg instanceof RubyHash) {
            //TODO
        } else if (msg instanceof RubyBuffer) {
            eb.publish(addr, ((RubyBuffer) msg).getBuffer());
        } else if (msg instanceof RubyFloat) {
            eb.publish(addr, RubyNumeric.num2dbl(msg));
        } else if (msg instanceof RubyFixnum) {
            eb.publish(addr, RubyNumeric.num2long(msg));
        } else
            throw context.runtime.newArgumentError("Unknown type of message " + msg.getType());
        return context.runtime.getNil();
    }

    @JRubyMethod(name="register_handler", required = 1, optional = 1, module = true)
    public static IRubyObject registerHandler(final ThreadContext context, IRubyObject recv, IRubyObject[] args, final Block blk) {
        String id = UUID.randomUUID().toString();
        Handler<Message> handler = newMessageHandler(context, blk);
        if (args.length > 1 && args[1].isTrue())
            eb.registerLocalHandler(id, handler);
        else
            eb.registerHandler(id, handler);
        handlerMap.put(id, handler);
        return context.runtime.newString(id);
    }

    @JRubyMethod(name="unregister_handler", module = true)
    public static IRubyObject unregisterHandler(ThreadContext context, IRubyObject recv, IRubyObject handlerId) {
        // Fail silently
        Handler<? extends Message> handler = handlerMap.remove(handlerId.asJavaString());
        if (handler != null)
            eb.unregisterHandler(handlerId.asJavaString(), handler);
        return context.runtime.getNil();
     }
    public static String registerSimpleHandler(Handler<? extends Message> handler) {
        return registerSimpleHandler(false, handler);
    }

    public static String registerSimpleHandler(boolean local, Handler<? extends Message> handler) {
        String id = UUID.randomUUID().toString();
        if (local)
            eb.registerLocalHandler(id, handler);
        else
            eb.registerHandler(id, handler);

        handlerMap.put(id, handler);
        return id;
    }

    public static String unregisterHandler(String id) {
        handlerMap.remove(id);
        return id;
    }

    private static Handler<Message> newMessageHandler(final ThreadContext context, final Block blk) {
        return new Handler<Message>() {
            @Override
            public void handle(Message message) {
                RubyModule vertxModule = context.runtime.getModule("Vertx");
                RubyClass rubyMessageClass = (RubyClass) vertxModule.getClass("Message");
                RubyMessage msg = (RubyMessage) rubyMessageClass.allocate();
                msg.setMessage(message);
                blk.call(context, msg);
            }
        };
    }
}
