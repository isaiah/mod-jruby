package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.util.TypeConverter;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.sockjs.SockJSServer;
import org.vertx.java.core.sockjs.SockJSSocket;
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

    @JRubyMethod(name = "install_app")
    public IRubyObject installApp(final ThreadContext context, IRubyObject config, final Block blk) {
        final Ruby runtime = context.runtime;
        IRubyObject tmp = TypeConverter.convertToTypeWithCheck(config, runtime.getHash(), "to_hash");
        if (!tmp.isNil()) {
            this.server.installApp(new JsonObject((RubyHash) tmp), new Handler<SockJSSocket>() {
                @Override
                public void handle(SockJSSocket sockJSSocket) {
                    RubyClass sockjsSocketClass = (RubyClass) runtime.getClassFromPath("Vertx::SockJSSocket");
                    blk.call(context, new RubySockJSSocket(runtime, sockjsSocketClass, sockJSSocket));
                }
            });
        }
        return this;
    }

    @JRubyMethod
    public IRubyObject bridge(ThreadContext context, IRubyObject[] args) {
        final Ruby runtime = context.runtime;
        IRubyObject conf = TypeConverter.convertToTypeWithCheck(args[0], runtime.getHash(), "to_hash");
        long authTimeout = 5 * 60 * 1000;
        if (args.length > 3) {
            authTimeout = RubyNumeric.num2long(args[3]);
        }
        if (!conf.isNil()) {
            this.server.bridge(new JsonObject((RubyHash) conf), new JsonArray((RubyArray) args[1]), new JsonArray((RubyArray) args[2]), authTimeout, (args.length > 4) ? args[4].asJavaString() : null);
        }
        return this;
    }
}
