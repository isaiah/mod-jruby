package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;

/**
 * Created by isaiah on 12/16/13.
 */
public class RubyMessage extends RubyObject {
    private Message message;

    public static RubyClass createMessageClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass klazz = vertxModule.defineClassUnder("Message", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyMessage(ruby, rubyClass);
            }
        });
        klazz.defineAnnotatedMethods(RubyMessage.class);
        return klazz;
    }

    public RubyMessage(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @JRubyMethod
    public IRubyObject body(ThreadContext context) {
        Object body = this.message.body();
        if (body instanceof JsonObject)
            return context.runtime.getNil();
            // return RubyHash
        else if (body instanceof Buffer) {
            RubyClass rubyBufferClass = (RubyClass) context.runtime.getClassFromPath("Vertx::Buffer");
            RubyBuffer buff = (RubyBuffer) rubyBufferClass.allocate();
            buff.setBuffer((Buffer) body);
            return buff;
        } else
            return context.runtime.getNil();

    }

    @JRubyMethod
    public IRubyObject address(ThreadContext context) {
        return context.runtime.newString(this.message.address());
    }

    @JRubyMethod
    public IRubyObject reply(ThreadContext context, IRubyObject msg, Block blk) {
        if (msg instanceof RubyHash) {
            //TODO
        } else if (msg instanceof RubyBuffer) {
            this.message.reply(((RubyBuffer) msg).getBuffer());
        } else if (msg instanceof RubyFloat) {
            this.message.reply(RubyNumeric.num2dbl(msg));
        } else if (msg instanceof RubyFixnum) {
            this.message.reply(RubyNumeric.num2long(msg));
        } else
            throw context.runtime.newArgumentError("Unknown type of message " + msg.getType());
        return this;
    }

}
