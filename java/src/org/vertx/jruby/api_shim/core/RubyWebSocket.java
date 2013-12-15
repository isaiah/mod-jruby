package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyClass;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * Created by isaiah on 12/15/13.
 */
@JRubyClass(name="WebSocket")
public class RubyWebSocket extends RubyObject {
    public static void createWebSocketClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("WebSocket", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyWebSocket(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyWebSocket.class);
    }

    public RubyWebSocket(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }
}
