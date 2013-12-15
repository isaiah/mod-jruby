package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyHash;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.anno.JRubyModule;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by isaiah on 12/15/13.
 */
@JRubyModule(name="EventBus")
public class RubyEventBus {
    private static Map handlerMap = new HashMap<String, Block>();
    private static EventBus eb = JRubyVerticleFactory.vertx.eventBus();

    public static RubyModule createEventBusModule(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyModule klazz = vertxModule.defineModuleUnder("EventBus");
        klazz.defineAnnotatedMethods(RubyEventBus.class);
        return klazz;
    }

    @JRubyMethod(name="send", required=2, optional=1, module=true, rest=true)
    public static IRubyObject send(ThreadContext context, IRubyObject recv, IRubyObject[] args) {
        IRubyObject msg = args[1];
        String addr = args[0].asJavaString();
        if (msg instanceof RubyHash) {
            Map map = new HashMap<>();
            for(RubyHash.RubyHashEntry entry : (RubyHash) msg) {
                map.put(entry.getJavaifiedKey(), entry.getJavaifiedValue());
            }
            eb.send(addr, map);
        } else if (msg instanceof RubyBuffer) {
            eb.send(addr, msg.asJavaString());
        }

    }

}
