package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerFileUpload;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

/**
 * Created by isaiah on 12/14/13.
 */
@JRubyClass(name="HttpServerRequest", include="ReadStream")
public class RubyHttpServerRequest extends RubyObject {
    private HttpServerRequest request;

    public static void createHttpServerRequestClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass hsr = vertxModule.defineClassUnder("HttpServerRequest", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpServerRequest(ruby, rubyClass);
            }
        });
    }

    public RubyHttpServerRequest(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }

    @JRubyMethod(visibility = Visibility.PRIVATE)
    public IRubyObject initialize(ThreadContext context) {
        return this;
    }

    public void setHttpServerRequest(HttpServerRequest request) {
        this.request = request;
    }

    public HttpServerRequest httpServerRequest() {
        return this.request;
    }

    @JRubyMethod
    public IRubyObject version(ThreadContext context) {
        return context.runtime.newString(this.request.version().toString());
    }

    @JRubyMethod
    public IRubyObject method(ThreadContext context) {
        return context.runtime.newString(this.request.method());
    }

    @JRubyMethod
    public IRubyObject uri(ThreadContext context) {
        return context.runtime.newString(this.request.uri());
    }

    @JRubyMethod
    public IRubyObject path(ThreadContext context) {
        return context.runtime.newString(this.request.path());
    }

    @JRubyMethod
    public IRubyObject query(ThreadContext context) {
        return context.runtime.newString(this.request.query());
    }

    @JRubyMethod
    public IRubyObject params(ThreadContext context) {
        // FIXME: return MultiMap
        return context.runtime.getNil();
    }

    @JRubyMethod
    public IRubyObject response(ThreadContext context) {
        RubyModule vertxModule = context.runtime.getModule("Vertx");
        RubyClass rubyHttpServerResponseClass = (RubyClass) vertxModule.getClass("HttpServerResponse");
        RubyHttpServerResponse resp = (RubyHttpServerResponse) rubyHttpServerResponseClass.allocate();
        resp.setResponse(this.request.response());
        return resp;
    }

    @JRubyMethod
    public IRubyObject headers(ThreadContext context) {
        //FIXME: return MultiMap
        return context.runtime.getNil();
    }

    @JRubyMethod(name="expect_multipart!")
    public IRubyObject epect_multipart_bang(ThreadContext context) {
        this.request.expectMultiPart(true);
        return context.runtime.getNil();
    }

    @JRubyMethod(name="form_attributes")
    public IRubyObject getFormAttributes(ThreadContext context) {
        // FIXME: MultiMap of this.request.formAttributes()
        return context.runtime.getNil();
    }

    @JRubyMethod(name="remote_address")
    public IRubyObject getRemoteAddress(ThreadContext context) {
        return context.runtime.newString(this.request.remoteAddress().toString());
    }

    @JRubyMethod(name = "absolute_uri")
    public IRubyObject getAbsoluteUri(ThreadContext context) {
        //TODO: wrapper with a jruby RubyURI class?
        return context.runtime.newString(this.request.absoluteURI().toString());
    }

    @JRubyMethod(name="upload_handler")
    public IRubyObject uploadHandler(final ThreadContext context, final Block blk) {
        this.request.uploadHandler(new Handler<HttpServerFileUpload>() {
            @Override
            public void handle(HttpServerFileUpload httpServerFileUpload) {
                RubyModule vertxModule = context.runtime.getModule("Vertx");
                RubyClass httpServerFileUploadClass = (RubyClass) vertxModule.getClass("HttpServerFileUpload");
                RubyHttpServerFileUpload fileUpload = (RubyHttpServerFileUpload) httpServerFileUploadClass.allocate();
                fileUpload.setHttpServerFileUpload(httpServerFileUpload);
                blk.call(context, fileUpload);
            }
        });
        return this;
    }

    @JRubyMethod(name="body_handler")
    public IRubyObject bodyHandler(final ThreadContext context, final Block blk) {
        this.request.bodyHandler(new Handler<Buffer>() {
            @Override
            public void handle(Buffer buffer) {
                RubyModule vertxModule = context.runtime.getModule("Vertx");
                RubyClass rubyBufferClass = (RubyClass) vertxModule.getClass("Buffer");
                RubyBuffer rubyBuffer = (RubyBuffer) rubyBufferClass.allocate();
                rubyBuffer.setBuffer(buffer);
                blk.call(context, rubyBuffer);
            }
        });
        return this;
    }

}
