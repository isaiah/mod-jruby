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
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpClientResponse;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyHttpClientResponse extends RubyObject {
    private HttpClientResponse response;

    public static RubyClass createHttpClientResponse(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("HttpClientResponse", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpClientResponse(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyHttpClientResponse.class);
        return klazz;
    }

    public RubyHttpClientResponse(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public void setHttpClientResponse(HttpClientResponse response) {
        this.response = response;
    }

    public HttpClientResponse httpClientResponse() {
        return this.response;
    }

    @JRubyMethod(name = "status_code")
    public IRubyObject statusCode(ThreadContext context) {
        return context.runtime.newFixnum(this.response.statusCode());
    }

    @JRubyMethod(name = "status_message")
    public IRubyObject statusMessage(ThreadContext context) {
        return context.runtime.newString(this.response.statusMessage());
    }

    @JRubyMethod
    public IRubyObject header(ThreadContext context, IRubyObject key) {
        return context.runtime.newString(this.response.headers().get(key.asJavaString()));
    }

    @JRubyMethod
    public IRubyObject headers(ThreadContext context) {
        return context.runtime.getNil();
    }

    @JRubyMethod(name = "body_handler")
    public IRubyObject bodyHandler(final ThreadContext context, final Block blk) {
        this.response.bodyHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer buffer) {
                blk.call(context, JRubyUtils.newBuffer(context.runtime, buffer));
            }
        });
        return this;
    }
}
