package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.ext.socket.Addrinfo;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.Visibility;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.impl.JRubyVerticleFactory;

import java.net.InetAddress;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyNetSocket extends RubyObject {
    private NetSocket sock;
    private String localAddr;
    private String remoteAddr;
    private String writeHandlerId;
    private Block closeHandler;

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
        this.writeHandlerId = RubyEventBus.registerSimpleHandler(new Handler<Message>() {
            @Override
            public void handle(Message message) {
                //write(message.body());
            }
        });
    }

    public NetSocket netSocket() {
        return this.sock;
    }

    @JRubyMethod(visibility = Visibility.PRIVATE)
    public IRubyObject initialize(final ThreadContext context) {
        this.sock.closeHandler(new Handler<Void>() {
            @Override
            public void handle(Void aVoid) {
                RubyEventBus.unregisterHandler(writeHandlerId);
                if (closeHandler != null) closeHandler.call(context);
            }
        });
        return this;
    }

    @JRubyMethod(name = "write_str", required = 1, optional = 1)
    public IRubyObject writeString(ThreadContext context, IRubyObject[] args) {
        this.sock.write(args[0].asJavaString(), args.length > 1 ? args[1].asJavaString() : "UTF-8");
        return this;
    }

    @JRubyMethod(name = "close_handler")
    public IRubyObject closeHandler(final ThreadContext context, final Block blk) {
        this.closeHandler = blk;
        return this;
    }

    @JRubyMethod(name = "send_file")
    public IRubyObject sendFile(ThreadContext context, IRubyObject filePath) {
        this.sock.sendFile(filePath.asJavaString());
        return this;
    }

    @JRubyMethod
    public IRubyObject close(ThreadContext context) {
        this.sock.close();
        return context.runtime.getNil();
    }

    @JRubyMethod
    public IRubyObject writeHandlerId(ThreadContext context) {
        return context.runtime.newString(this.writeHandlerId);
    }

    @JRubyMethod(name = "remote_address")
    public IRubyObject remoteAddress(ThreadContext context) {
        Ruby runtime = context.runtime;
        InetAddress remoteAddr = this.sock.remoteAddress().getAddress();
        return new Addrinfo(runtime, runtime.getClass("Addrinfo"), remoteAddr);
    }

    @JRubyMethod(name = "local_address")
    public IRubyObject localAddress(ThreadContext context) {
        Ruby runtime = context.runtime;
        InetAddress localAddr = this.sock.localAddress().getAddress();
        return new Addrinfo(runtime, runtime.getClass("Addrinfo"), localAddr);
    }
}
