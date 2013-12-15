package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyClass;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.http.HttpServerFileUpload;

/**
 * Created by isaiah on 12/15/13.
 */
@JRubyClass(name="HttpServerFileUpload")
public class RubyHttpServerFileUpload extends RubyObject {
    private HttpServerFileUpload upload;

    public static void createHttpServerFileUploadClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("HttpServerFileUpload", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyHttpServerFileUpload(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyHttpServerFileUpload.class);
    }

    public RubyHttpServerFileUpload(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }

    public void setHttpServerFileUpload(HttpServerFileUpload upload) {
        this.upload = upload;
    }
}
