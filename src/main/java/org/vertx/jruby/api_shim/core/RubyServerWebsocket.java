package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyClass;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.http.ServerWebSocket;

/**
 * Created by isaiah on 12/14/13.
 */
@JRubyClass(name="ServerWebSocket", parent="WebSocket", include={"ReadStream", "WriteStream"})
public class RubyServerWebsocket extends RubyWebSocket {
    private ServerWebSocket serverWebsocket;

    public static void createRubyServerWebsocketClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("ServerWebsocket", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyServerWebsocket(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyServerWebsocket.class);
    }

    public RubyServerWebsocket(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }

    public void setServerWebsocket(ServerWebSocket websocket) {
        this.serverWebsocket = websocket;
    }
}
