package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.vertx.java.core.http.HttpServerRequest;

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
}
