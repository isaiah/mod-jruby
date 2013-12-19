package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.sockjs.SockJSSocket;

/**
 * Created by isaiah on 12/19/13.
 */
public class RubySockJSSocket extends RubyObject {
    private SockJSSocket socket;
    private String handlerId;

    public static RubyClass createSockJSSocketClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("SockJSSocket", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubySockJSSocket(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubySockJSSocket.class);
        return klazz;
    }

    private RubySockJSSocket(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public RubySockJSSocket(Ruby ruby, RubyClass rubyClass, SockJSSocket sockJSSocket) {
        super(ruby, rubyClass);
        this.socket = sockJSSocket;
        this.handlerId = RubyEventBus.registerSimpleHandler(new Handler<Message>() {
            @Override
            public void handle(Message message) {
                //write(message.body());
            }
        });
    }

    @JRubyMethod
    public IRubyObject initialize(ThreadContext context) { return this; }

    @JRubyMethod
    public IRubyObject close(ThreadContext context) {
        RubyEventBus.unregisterHandler(this.handlerId);
        this.socket.close();
        return context.runtime.getNil();
    }

    @JRubyMethod(name = "handler_id")
    public IRubyObject handlerId(ThreadContext context) {
        return context.runtime.newString(this.handlerId);
    }
}
