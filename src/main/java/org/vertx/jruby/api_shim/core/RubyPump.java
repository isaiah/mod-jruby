package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyClass;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.streams.Pump;

/**
 * Created by isaiah on 12/19/13.
 */
@JRubyClass(name = "Pump")
public class RubyPump extends RubyObject {
    private Pump pump;

    public static RubyClass createPumpClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("Pump", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyPump(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyPump.class);
        return klazz;
    }

    public RubyPump(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    @JRubyMethod(name = "write_queue_max_size=")
    public IRubyObject setWriteQueueMaxSize(ThreadContext context, IRubyObject size) {
        this.pump.setWriteQueueMaxSize(RubyNumeric.num2int(size));
        return context.runtime.getNil();
    }

    @JRubyMethod
    public IRubyObject start(ThreadContext context) {
        this.pump.start();
        return this;
    }

    @JRubyMethod
    public IRubyObject stop(ThreadContext context) {
        this.pump.stop();
        return this;
    }

    @JRubyMethod(name = "bytes_pumped")
    public IRubyObject bytesPumped(ThreadContext context) {
        return context.runtime.newFixnum(this.pump.bytesPumped());
    }
}
