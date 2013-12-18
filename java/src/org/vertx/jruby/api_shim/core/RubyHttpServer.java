package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

/**
 * Created by isaiah on 12/14/13.
 */
public class RubyHttpServer extends RubyObject {
    private HttpServer httpServer;
    private boolean compress;

    public static void createHttpServerClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass httpServerClass = vertxModule.defineClassUnder("HttpServer", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpServer(ruby, rubyClass);
            }
        });
        httpServerClass.defineAnnotatedMethods(RubyHttpServer.class);
    }

    public RubyHttpServer(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }

    @JRubyMethod(optional=1)
    public IRubyObject initialize(ThreadContext context, IRubyObject[] args) {
        if (args.length > 0)
                this.compress = JRubyUtils.getBooleanFromRubyHash(context, args[0], "compress");
        this.httpServer = JRubyVerticleFactory.vertx.createHttpServer();
        return this;
    }

    // FIXME: route matcher not ready
    @JRubyMethod(name="request_handler", optional = 1)
    public IRubyObject requestHandler(final ThreadContext context, IRubyObject arg, final Block blk) {
        final Ruby runtime = context.runtime;
        this.httpServer.requestHandler(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyClass rubyHttpServerRequestClass = (RubyClass) runtime.getClassFromPath("Vertx::HttpServerRequest");
                RubyHttpServerRequest request = (RubyHttpServerRequest) rubyHttpServerRequestClass.allocate();
                request.setHttpServerRequest(httpServerRequest);
                blk.call(context, request);
            }
        });
        return this;
    }

    @JRubyMethod(name="websocket_handler")
    public IRubyObject websocketHandler(final ThreadContext context, final Block blk) {
        this.httpServer.websocketHandler(new Handler<ServerWebSocket>() {
            @Override
            public void handle(ServerWebSocket serverWebSocket) {
                RubyClass rubyServerWebsocketClass = (RubyClass) context.runtime.getClassFromPath("Vertx::ServerWebsocket");
                RubyServerWebsocket websocket = (RubyServerWebsocket) rubyServerWebsocketClass.allocate();
                websocket.setServerWebsocket(serverWebSocket);
                blk.call(context, websocket);
            }
        });
        return this;
    }

    @JRubyMethod(name="compression=")
    public IRubyObject setCompression(ThreadContext context, IRubyObject supported) {
        this.httpServer.setCompressionSupported(supported.isTrue());
        return context.runtime.getNil();
    }

    @JRubyMethod(name={"compression?", "compression"})
    public IRubyObject getCompression(ThreadContext context) {
        return context.runtime.newBoolean(this.httpServer.isCompressionSupported());
    }

    @JRubyMethod(required = 1, optional = 1)
    public IRubyObject listen(ThreadContext context, IRubyObject[] args, final Block blk) {
        String host = "0.0.0.0";
        if (args.length > 1)
            host = args[1].asJavaString();
        if (blk.isGiven())
            // FIXME: ARWrapperHandler
            this.httpServer.listen(RubyNumeric.num2int(args[0]), host);
        else
            this.httpServer.listen(RubyNumeric.num2int(args[0]), host);
        return this;
    }
    @JRubyMethod
    public IRubyObject close(ThreadContext context, Block blk) {
        // FIXME: ARWrapperHandler
        this.httpServer.close();
        return context.runtime.getNil();
    }

    @JRubyMethod(name="max_websocket_frame_size=")
    public IRubyObject setMaxWebsocketFrameSize(ThreadContext context, IRubyObject size) {
        this.httpServer.setMaxWebSocketFrameSize(RubyNumeric.num2int(size));
        return this;
    }

    @JRubyMethod(name="max_websocket_frame_size")
    public IRubyObject getMaxWebsocketMaxFrameSize(ThreadContext context) {
        return context.runtime.newFixnum(this.httpServer.getMaxWebSocketFrameSize());
    }
}
