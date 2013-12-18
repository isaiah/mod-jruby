package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.net.NetSocket;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyNetSocket extends RubyObject {
    private NetSocket sock;

    public static RubyClass createNetSocketClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("NetSocket", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyNetSocket(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyNetSocket.class);
        return klazz;
    }

    public RubyNetSocket(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public void setNetSocket(NetSocket sock) {
        this.sock = sock;
    }

    public NetSocket netSocket() {
        return this.sock;
    }
}
