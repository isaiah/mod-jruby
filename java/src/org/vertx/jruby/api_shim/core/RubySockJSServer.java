package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubySockJSServer extends RubyObject {
    private SockJSServer server;

    public static RubyClass createSockJSServerClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("SockJSServer", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubySockJSServer(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubySockJSServer.class);
        return klazz;
    }

    public RubySockJSServer(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    @JRubyMethod
    public IRubyObject initialize(ThreadContext context, IRubyObject httpServer) {
        this.server = JRubyVerticleFactory.vertx.createSockJSServer(((RubyHttpServer) httpServer).httpServer());
        return this;
    }

    @JRubyMethod
    public IRubyObject bridge(ThreadContext context, IRubyObject[] args) {
        // TODO
        return this;
    }
}
