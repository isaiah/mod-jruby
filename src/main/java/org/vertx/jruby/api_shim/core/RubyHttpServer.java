package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.omg.CosNaming._NamingContextExtStub;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.platform.impl.JRubyVerticleFactory;
import org.vertx.jruby.api_shim.RubySSLSupport;

/**
 * Created by isaiah on 12/14/13.
 */
public class RubyHttpServer extends RubyObject implements RubySSLSupport<RubyHttpServer> {
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

    public RubyHttpServer(Ruby ruby, RubyClass rubyClass, HttpServer httpServer) {
        super(ruby, rubyClass);
        this.httpServer = httpServer;
    }

    public HttpServer httpServer() {
        return this.httpServer;
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
    public IRubyObject listen(final ThreadContext context, IRubyObject[] args, final Block blk) {
        final Ruby runtime = context.runtime;
        String host = "0.0.0.0";
        if (args.length > 1)
            host = args[1].asJavaString();
        if (blk.isGiven())
            // FIXME: ARWrapperHandler
            this.httpServer.listen(RubyNumeric.num2int(args[0]), host, new Handler<AsyncResult<HttpServer>>() {
                @Override
                public void handle(AsyncResult<HttpServer> httpServerAsyncResult) {
                    if (httpServerAsyncResult.succeeded()) {
                        RubyClass rubyHttpServerClass = (RubyClass) runtime.getClassFromPath("Vertx::HttpServer");
                        blk.call(context, runtime.getNil(), new RubyHttpServer(runtime, rubyHttpServerClass, httpServerAsyncResult.result()));
                    } else {
                        blk.call(context, new NativeException(runtime, runtime.getNativeException(), httpServerAsyncResult.cause()));
                    }
                }
            });
        else
            this.httpServer.listen(RubyNumeric.num2int(args[0]), host);
        return this;
    }
    @JRubyMethod
    public IRubyObject close(final ThreadContext context, final Block blk) {
        final Ruby runtime = context.runtime;
        if (blk.isGiven()) {
            this.httpServer.close(new Handler<AsyncResult<Void>>() {
                @Override
                public void handle(AsyncResult<Void> voidAsyncResult) {
                    if (voidAsyncResult.succeeded())
                        blk.call(context);
                    else
                        blk.call(context);
                }
            });
        } else {
            this.httpServer.close();
        }
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

    @JRubyMethod(name = "ssl=")
    @Override
    public RubyHttpServer setSSL(ThreadContext context, IRubyObject ssl) {
        this.httpServer.setSSL(ssl.isTrue());
        return this;
    }

    @JRubyMethod(name = "ssl?")
    @Override
    public IRubyObject isSSL(ThreadContext context) {
        return context.runtime.newBoolean(this.httpServer.isSSL());
    }

    @JRubyMethod(name =  "key_store_path=")
    @Override
    public RubyHttpServer setKeyStorePath(ThreadContext context, IRubyObject path) {
        this.httpServer.setKeyStorePath(path.asJavaString());
        return this;
    }

    @JRubyMethod(name = "key_store_path")
    @Override
    public IRubyObject keyStorePath(ThreadContext context) {
        return context.runtime.newString(this.httpServer.getKeyStorePath());
    }

    @JRubyMethod(name = "key_store_password=")
    @Override
    public RubyHttpServer setKeyStorePassword(ThreadContext context, IRubyObject password) {
        this.httpServer.setKeyStorePassword(password.asJavaString());
        return this;
    }

    @JRubyMethod(name = "key_store_password")
    @Override
    public IRubyObject keyStorePassword(ThreadContext context) {
        return context.runtime.newString(this.httpServer.getKeyStorePassword());
    }

    @JRubyMethod(name = "trust_store_path=")
    @Override
    public RubyHttpServer setTrustStorePath(ThreadContext context, IRubyObject path) {
        this.httpServer.setTrustStorePath(path.asJavaString());
        return this;
    }

    @JRubyMethod(name = "trust_store_path")
    @Override
    public IRubyObject trustStorePath(ThreadContext context) {
        return context.runtime.newString(this.httpServer.getTrustStorePath());
    }

    @JRubyMethod(name = "trust_store_password=")
    @Override
    public RubyHttpServer setTrustStorePassword(ThreadContext context, IRubyObject password) {
        this.httpServer.setKeyStorePassword(password.asJavaString());
        return this;
    }

    @JRubyMethod(name = "trust_store_password")
    @Override
    public IRubyObject trustStorePassword(ThreadContext context) {
        return context.runtime.newString(this.httpServer.getTrustStorePassword());
    }

    @JRubyMethod(name = "client_auth_required=")
    public IRubyObject setClientAuthRequired(ThreadContext context, IRubyObject val) {
        this.httpServer.setClientAuthRequired(val.isTrue());
        return this;
    }

    @JRubyMethod(name = "client_auth_required?")
    public IRubyObject clientAuthRequired(ThreadContext context) {
        return context.runtime.newBoolean(this.httpServer.isClientAuthRequired());
    }
}
