package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpClient;
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.http.WebSocket;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyHttpClient extends RubyObject {
    private HttpClient httpClient;
    private boolean compression;
    public static RubyClass createHttpClientClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("HttpClient", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpClient(ruby, rubyClass);
            }
        });

        klazz.defineAnnotatedMethods(RubyHttpClient.class);
        return klazz;
    }

    public RubyHttpClient(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    @JRubyMethod(optional = 1)
    public IRubyObject initialize(ThreadContext context, IRubyObject[] args) {
        if (args.length > 0)
        this.compression = JRubyUtils.getBooleanFromRubyHash(context, args[0], "compress");
        this.httpClient = JRubyVerticleFactory.vertx.createHttpClient();
        return this;
    }

    @JRubyMethod(name="compression=")
    public IRubyObject setCompression(ThreadContext context, IRubyObject compression) {
        this.httpClient.setTryUseCompression(compression.isTrue());
        return context.runtime.getNil();
    }

    @JRubyMethod(name={"compression", "compression?"})
    public IRubyObject isCompression(ThreadContext context) {
        return context.runtime.newBoolean(this.httpClient.getTryUseCompression());
    }

    @JRubyMethod(name = "exception_handler")
    public IRubyObject exceptionHandler(final ThreadContext context, final Block blk) {
        final Ruby runtime = context.runtime;
        this.httpClient.exceptionHandler(new Handler<Throwable>() {
            @Override
            public void handle(Throwable throwable) {
                blk.call(context, new NativeException(runtime, runtime.getNativeException(), throwable));
            }
        });
        return this;
    }

    @JRubyMethod(name = "max_pool_size=")
    public IRubyObject setMaxPollSize(ThreadContext context, IRubyObject size) {
        this.httpClient.setMaxPoolSize(RubyNumeric.num2int(size));
        return this;
    }

    @JRubyMethod(name = "max_pool_size")
    public IRubyObject maxPoolSize(ThreadContext context) {
        return context.runtime.newFixnum(this.httpClient.getMaxPoolSize());
    }

    @JRubyMethod(name = "keep_alive=")
    public IRubyObject setKeepAlive(ThreadContext context, IRubyObject bool) {
        this.httpClient.setKeepAlive(bool.isTrue());
        return this;
    }

    @JRubyMethod(name = "keep_alive")
    public IRubyObject isKeepAlive(ThreadContext context) {
        return context.runtime.newBoolean(this.httpClient.isKeepAlive());
    }

    @JRubyMethod(name = "port=")
    public IRubyObject setPort(ThreadContext context, IRubyObject port) {
        this.httpClient.setPort(RubyNumeric.num2int(port));
        return this;
    }

    @JRubyMethod(name = "port")
    public IRubyObject port(ThreadContext context) {
        return context.runtime.newFixnum(this.httpClient.getPort());
    }

    @JRubyMethod(name = "host=")
    public IRubyObject setHost(ThreadContext context, IRubyObject host) {
        this.httpClient.setHost(host.asJavaString());
        return this;
    }

    @JRubyMethod(name = "host")
    public IRubyObject host(ThreadContext context) {
        return context.runtime.newString(this.httpClient.getHost());
    }

    @JRubyMethod(name = "verify_host=")
    public IRubyObject setVerifyHost(ThreadContext context, IRubyObject bool) {
        this.httpClient.setVerifyHost(bool.isTrue());
        return this;
    }

    @JRubyMethod(name = "verify_host")
    public IRubyObject isVerifyHost(ThreadContext context) {
        return context.runtime.newBoolean(this.httpClient.isVerifyHost());
    }

    @JRubyMethod(name = "max_websocket_frame_size=")
    public IRubyObject setMaxWebsocketFrameSize(ThreadContext context, IRubyObject size) {
        this.httpClient.setMaxWebSocketFrameSize(RubyNumeric.num2int(size));
        return this;
    }

    @JRubyMethod(name = "max_websocket_frame_size")
    public IRubyObject maxWebsocketFrameSize(ThreadContext context) {
        return context.runtime.newFixnum(this.httpClient.getMaxWebSocketFrameSize());
    }

    @JRubyMethod(name = "connect_web_socket")
    public IRubyObject connectWebSocket(final ThreadContext context, IRubyObject uri, final Block blk) {
        this.httpClient.connectWebsocket(uri.asJavaString(), new Handler<WebSocket>() {
            @Override
            public void handle(WebSocket webSocket) {
                blk.call(context, JRubyUtils.newWebSocket(context.runtime, webSocket));
            }
        });
        return this;
    }

    @JRubyMethod(name = "get_now", required = 1, optional = 1)
    public IRubyObject getNow(final ThreadContext context, IRubyObject[] args, final Block blk) {
        if (args.length == 1) {
            this.httpClient.getNow(args[0].asJavaString(), createResponseHandler(context, blk));
        } else {
            // TODO pass headers as MultiMap
        }
        return this;
    }

    @JRubyMethod(name = "options")
    public IRubyObject options(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.options(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "head")
    public IRubyObject head(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.head(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }
    @JRubyMethod(name = "get")
    public IRubyObject get(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.get(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "post")
    public IRubyObject post(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.post(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "put")
    public IRubyObject put(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.put(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "delete")
    public IRubyObject delete(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.delete(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "trace")
    public IRubyObject trace(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.trace(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "connect")
    public IRubyObject connect(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.connect(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod(name = "patch")
    public IRubyObject patch(final ThreadContext context, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.patch(uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod
    public IRubyObject request(final ThreadContext context, IRubyObject method, IRubyObject uri, final Block blk) {
        HttpClientRequest request = this.httpClient.request(method.asJavaString(), uri.asJavaString(), createResponseHandler(context, blk));
        return JRubyUtils.newHttpClientRequest(context.runtime, request);
    }

    @JRubyMethod
    public IRubyObject close(ThreadContext context) {
        this.httpClient.close();
        return context.runtime.getNil();
    }

    private Handler<HttpClientResponse> createResponseHandler(final ThreadContext context, final Block blk) {
        return new Handler<HttpClientResponse>() {
            @Override
            public void handle(HttpClientResponse httpClientResponse) {
                blk.call(context, JRubyUtils.newHttpClientResponse(context.runtime, httpClientResponse));
            }
        };
    }
}
