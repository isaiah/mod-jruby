package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.http.HttpClientRequest;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyHttpClientRequest extends RubyObject {
    private HttpClientRequest request;

    public static RubyClass createHttpClientRequest(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("HttpClientResponse", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpClientRequest(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyHttpClientRequest.class);
        return klazz;
    }

    public RubyHttpClientRequest(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public void setHttpClientRequest(HttpClientRequest request) {
        this.request = request;
    }

    public HttpClientRequest httpClientRequest() {
        return this.request;
    }
}
