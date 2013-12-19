package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.net.NetClient;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.impl.JRubyVerticleFactory;
import org.vertx.jruby.api_shim.RubySSLSupport;

/**
 * Created by isaiah on 18/12/2013.
 */
public class RubyNetClient extends RubyObject implements RubySSLSupport<RubyNetClient> {
    private NetClient netClient;

    public static RubyClass createNetClientClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.getOrCreateModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("NetClient", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyNetClient(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyNetClient.class);
        return klazz;
    }

    public RubyNetClient(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public void setNetClient(NetClient netClient) {
        this.netClient = netClient;
    }

    public NetClient netClient() {
        return this.netClient;
    }

    @JRubyMethod
    public IRubyObject initialize(ThreadContext context) {
        this.netClient = JRubyVerticleFactory.vertx.createNetClient();
        return this;
    }

    @JRubyMethod(required = 1, optional = 1)
    public IRubyObject connect(final ThreadContext context, IRubyObject[] args, final Block blk) {
        final Ruby runtime = context.runtime;
        String host = "localhost";
        if (args.length > 1)
            host = args[1].asJavaString();

        this.netClient.connect(RubyNumeric.num2int(args[0]), host, new AsyncResultHandler<NetSocket>() {
            @Override
            public void handle(AsyncResult<NetSocket> netSocketAsyncResult) {
                if (netSocketAsyncResult.succeeded())
                    blk.call(context, runtime.getNil(), JRubyUtils.newNetSocket(runtime, netSocketAsyncResult.result()));
                else
                    blk.call(context, new NativeException(runtime, runtime.getNativeException(), netSocketAsyncResult.cause()), runtime.getNil());
            }
        });
        return this;
    }

    @JRubyMethod(name = "reconnect_attempts=")
    public IRubyObject setReconnectAttempts(ThreadContext context, IRubyObject val) {
        this.netClient.setReconnectAttempts(RubyNumeric.num2int(val));
        return context.runtime.getNil();
    }

    @JRubyMethod(name = "reconnect_attempts")
    public IRubyObject reconnectAttempts(ThreadContext context) {
        return context.runtime.newFixnum(this.netClient.getReconnectAttempts());
    }

    @JRubyMethod(name = "reconnect_interval=")
    public IRubyObject setReconnectInterval(ThreadContext context, IRubyObject val) {
        this.netClient.setReconnectInterval(RubyNumeric.num2long(val));
        return context.runtime.getNil();
    }

    @JRubyMethod(name = "reconnect_interval")
    public IRubyObject reconnectInterval(ThreadContext context) {
        return context.runtime.newFixnum(this.netClient.getReconnectInterval());
    }

    @JRubyMethod(name = "connect_timeout=")
    public IRubyObject setConnectTimeout(ThreadContext context, IRubyObject val) {
        this.netClient.setConnectTimeout(RubyNumeric.num2int(val));
        return context.runtime.getNil();
    }

    @JRubyMethod(name = "connect_timeout")
    public IRubyObject connectTimeout(ThreadContext context) {
        return context.runtime.newFixnum(this.netClient.getConnectTimeout());
    }

    @JRubyMethod
    public IRubyObject close(ThreadContext context) {
        this.netClient.close();
        return context.runtime.getNil();
    }

    @JRubyMethod(name = "ssl=")
    @Override
    public RubyNetClient setSSL(ThreadContext context, IRubyObject ssl) {
        this.netClient.setSSL(ssl.isTrue());
        return this;
    }

    @JRubyMethod(name = "ssl?")
    @Override
    public IRubyObject isSSL(ThreadContext context) {
        return context.runtime.newBoolean(this.netClient.isSSL());
    }

    @JRubyMethod(name =  "kes_store_path=")
    @Override
    public RubyNetClient setKeyStorePath(ThreadContext context, IRubyObject path) {
        this.netClient.setKeyStorePath(path.asJavaString());
        return this;
    }

    @JRubyMethod(name = "key_store_path")
    @Override
    public IRubyObject keyStorePath(ThreadContext context) {
        return context.runtime.newString(this.netClient.getKeyStorePath());
    }

    @JRubyMethod(name = "key_store_password=")
    @Override
    public RubyNetClient setKeyStorePassword(ThreadContext context, IRubyObject password) {
        this.netClient.setKeyStorePassword(password.asJavaString());
        return this;
    }

    @JRubyMethod(name = "key_store_password")
    @Override
    public IRubyObject keyStorePassword(ThreadContext context) {
        return context.runtime.newString(this.netClient.getKeyStorePassword());
    }

    @JRubyMethod(name = "trust_store_path=")
    @Override
    public RubyNetClient setTrustStorePath(ThreadContext context, IRubyObject path) {
        this.netClient.setTrustStorePath(path.asJavaString());
        return this;
    }

    @JRubyMethod(name = "trust_store_path")
    @Override
    public IRubyObject trustStorePath(ThreadContext context) {
        return context.runtime.newString(this.netClient.getTrustStorePath());
    }

    @JRubyMethod(name = "trust_store_password=")
    @Override
    public RubyNetClient setTrustStorePassword(ThreadContext context, IRubyObject password) {
        this.netClient.setKeyStorePassword(password.asJavaString());
        return this;
    }

    @JRubyMethod(name = "trust_store_password")
    @Override
    public IRubyObject trustStorePassword(ThreadContext context) {
        return context.runtime.newString(this.netClient.getTrustStorePassword());
    }

    @JRubyMethod(name = "trust_all=")
    public IRubyObject setTrustAll(ThreadContext context, IRubyObject val) {
        this.netClient.setTrustAll(val.isTrue());
        return this;
    }

    @JRubyMethod(name = "trust_all?")
    public IRubyObject isTrustAll(ThreadContext context) {
        return context.runtime.newBoolean(this.netClient.isTrustAll());
    }
}
