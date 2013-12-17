package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.http.HttpServerResponse;

/**
 * Created by isaiah on 12/15/13.
 */
@JRubyClass(name="HttpServerResponse", include = "WriteStream")
public class RubyHttpServerResponse extends RubyObject {
    private HttpServerResponse response;

    public static void createHttpServerResponseClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("HttpServerResponse", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpServerResponse(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyHttpServerResponse.class);
    }

    public RubyHttpServerResponse(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }

    public void setResponse(HttpServerResponse response) {
        this.response = response;
    }

    @JRubyMethod(name="status=")
    public IRubyObject setStatus(ThreadContext context, IRubyObject status) {
        this.response.setStatusCode(RubyNumeric.num2int(status));
        return this;
    }

    @JRubyMethod(name="status")
    public IRubyObject status(ThreadContext context) {
        return context.runtime.newFixnum(this.response.getStatusCode());
    }

    @JRubyMethod(name="status_message=")
    public IRubyObject setStatusMessage(ThreadContext context, IRubyObject statusMessage) {
        this.response.setStatusMessage(statusMessage.asJavaString());
        return this;
    }

    @JRubyMethod(name="status_message")
    public IRubyObject statusMessage(ThreadContext context) {
        return context.runtime.newString(this.response.getStatusMessage());
    }

    @JRubyMethod(name="chunked=")
    public IRubyObject setChunked(ThreadContext context, IRubyObject chunked) {
        this.response.setChunked(chunked.isTrue());
        return this;
    }

    @JRubyMethod(name={"chunked", "chunked?"})
    public IRubyObject isChunked(ThreadContext context) {
        return context.runtime.newBoolean(this.response.isChunked());
    }

    @JRubyMethod
    public IRubyObject headers(ThreadContext context) {
        // FIXME: MultiMap
        return context.runtime.getNil();
    }

    @JRubyMethod
    public IRubyObject trailers(ThreadContext context) {
        // FIXME: MultiMap
        return context.runtime.getNil();
    }

    @JRubyMethod(name="put_header")
    public IRubyObject putHeader(ThreadContext context, IRubyObject key, IRubyObject value) {
        this.response.putHeader(key.asJavaString(), value.anyToString().asJavaString());
        return this;
    }

    @JRubyMethod(name="put_trailer")
    public IRubyObject putTrailer(ThreadContext context, IRubyObject key, IRubyObject value) {
        this.response.putTrailer(key.asJavaString(), value.anyToString().asJavaString());
        return this;
    }

    @JRubyMethod(name="write_str", required=1, optional=1)
    public IRubyObject writeString(ThreadContext context, IRubyObject[] args) {
        this.response.write(args[0].asJavaString(), (args.length > 1) ? args[1].asJavaString() : "UTF-8");
        return this;
    }

    @JRubyMethod(name="send_file", required=1, optional=1)
    public IRubyObject sendFile(ThreadContext context, IRubyObject[] args) {
        if (args.length > 1 && !args[1].isNil())
            this.response.sendFile(args[0].asJavaString(), args[1].asJavaString());
        else
            this.response.sendFile(args[0].asJavaString());

        return this;
    }

    @JRubyMethod(name="end", optional=1)
    public IRubyObject end(ThreadContext context, IRubyObject arg) {
        if (!arg.isNil()) {
            if (arg instanceof RubyString)
                this.response.end(arg.asJavaString());
            else if (arg instanceof RubyBuffer)
                this.response.end(((RubyBuffer)arg).getBuffer());
            else
                this.response.end();
        } else
            this.response.end();
        return this;
    }

    @JRubyMethod
    public IRubyObject close(ThreadContext context) {
        this.response.close();
        return context.runtime.getNil();
    }
}
