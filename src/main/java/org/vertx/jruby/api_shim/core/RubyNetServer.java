package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.net.NetServer;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyNetServer extends RubyObject {
    private NetServer netServer;

    public static RubyClass createNetServerClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("NetServer", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyNetServer(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyNetServer.class);
        return klazz;
    }

    public RubyNetServer(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    @JRubyMethod
    public IRubyObject initialize(ThreadContext context) {
        this.netServer = JRubyVerticleFactory.vertx.createNetServer();
        return this;
    }

    @JRubyMethod(name = "connect_handler")
    public IRubyObject connectHandler(final ThreadContext context, final Block blk) {
        this.netServer.connectHandler(new Handler<NetSocket>() {
            @Override
            public void handle(NetSocket netSocket) {
                blk.call(context, JRubyUtils.newNetSocket(context.runtime, netSocket));
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject close(final ThreadContext context, final Block blk) {
        if (blk.isGiven()) {
            this.netServer.close(new Handler<AsyncResult<Void>>() {
                @Override
                public void handle(AsyncResult<Void> voidAsyncResult) {
                    blk.call(context);
                }
            });
        } else {
            this.netServer.close();
        }
        return context.runtime.getNil();
    }

    @JRubyMethod
    public IRubyObject port(ThreadContext context) {
        return context.runtime.newFixnum(this.netServer.port());
    }

    @JRubyMethod
    public IRubyObject host(ThreadContext context) {
        return context.runtime.newString(this.netServer.host());
    }
}
