package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * Created by isaiah on 12/14/13.
 */
public class RubyRouteMatcher extends RubyObject {
    public static void createRubyRouteMatchClass(Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass rmClass = vertxModule.defineClassUnder("RouteMatcher", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return null;
            }
        });
        rmClass.defineAnnotatedMethods(RubyRouteMatcher.class);
    }

    public RubyRouteMatcher(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }
}
