package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.*;

/**
 * Created by isaiah on 17/12/2013.
 */
public class JRubyUtils {
    public static RubyHttpServerRequest newHttpServerRequest(Ruby runtime, HttpServerRequest request) {
        RubyClass klazz = (RubyClass) runtime.getClassFromPath("Vertx::HttpServerRequest");
        RubyHttpServerRequest req = (RubyHttpServerRequest) klazz.allocate();
        req.setHttpServerRequest(request);
        return req;
    }

    public static RubyHttpClientRequest newHttpClientRequest(Ruby runtime, HttpClientRequest request) {
        RubyClass klazz = (RubyClass) runtime.getClassFromPath("Vertx::HttpServerRequest");
        RubyHttpClientRequest req = (RubyHttpClientRequest) klazz.allocate();
        req.setHttpClientRequest(request);
        return req;
    }

    public static RubyWebSocket newWebSocket(Ruby runtime, WebSocket webSocket) {
        RubyClass klazz = (RubyClass) runtime.getClassFromPath("Vertx::WebSocket");
        RubyWebSocket sock = (RubyWebSocket) klazz.allocate();
        sock.setWebSocket(webSocket);
        return sock;
    }

    public static RubyHttpClientResponse newHttpClientResponse(Ruby runtime, HttpClientResponse response) {
        RubyClass klazz = (RubyClass) runtime.getClassFromPath("Vertx::HttpClientResponse");
        RubyHttpClientResponse resp = (RubyHttpClientResponse) klazz.allocate();
        resp.setHttpClientResponse(response);
        return resp;
    }

    public static RubyBuffer newBuffer(Ruby runtime, Buffer buffer) {
        RubyClass rubyBufferClass = (RubyClass) runtime.getClassFromPath("Vertx::Buffer");
        RubyBuffer rubyBuffer = (RubyBuffer) rubyBufferClass.allocate();
        rubyBuffer.setBuffer(buffer);
        return rubyBuffer;
    }

    public static boolean getBooleanFromRubyHash(ThreadContext context, IRubyObject hash, String key) {
        RubyHash options = hash.convertToHash();
        RubySymbol compressSym = context.runtime.newSymbol(key);
        if (options.has_key_p(compressSym).isTrue())
            return options.op_aref(context, compressSym).isTrue();
        return false;
    }
}
