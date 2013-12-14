package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.builtin.IRubyObject;
import org.jruby.runtime.ThreadContext;

import org.vertx.java.core.buffer.Buffer;

/**
 * Created with IntelliJ IDEA.
 * User: isaiah
 * Date: 13/12/2013
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class RubyBuffer extends RubyObject {
    private Buffer buffer;

    public static void createBufferClass(final Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass bufferClass = vertxModule.defineClassUnder("Buffer", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return new RubyBuffer(ruby, rubyClass);
            }
        });
    }

    private RubyBuffer(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    @JRubyMethod(name="initialize")
    public IRubyObject initialize(ThreadContext context, IRubyObject initialSizeHint) {
        this.buffer = new Buffer((int) initialSizeHint.convertToInteger().getLongValue());
        return this;
    }

    @JRubyMethod(name="initialize")
    public IRubyObject initialize(ThreadContext context) {
        this.buffer = new Buffer(0);
        return this;
    }

    @JRubyMethod(meta=true)
    public IRubyObject create(ThreadContext context, IRubyObject recv, IRubyObject initialSizeHint) {
        return this; //XXX
    }

    @JRubyMethod(name="create_from_str", meta=true)
    public IRubyObject createFromStr(ThreadContext context, IRubyObject recv, IRubyObject str, IRubyObject encoding) {
        return this; //XXX
    }

    @JRubyMethod(name="to_s")
    public IRubyObject toString(ThreadContext context) {
        return getRuntime().newString(this.buffer.toString("UTF-8"));
    }

    @JRubyMethod(name="get_byte")
    public IRubyObject getByte(ThreadContext context, IRubyObject position) {
        return getRuntime().newFixnum(this.buffer.getByte((int) position.convertToFloat().getLongValue()));
    }

    @JRubyMethod(name="get_fixnum")
    public IRubyObject getFixnum(ThreadContext context, IRubyObject position, IRubyObject offset) {
        long ret = 0;
        int pos = getIntVal(position);
        switch(getIntVal(offset)) {
            case 1:
                ret = this.buffer.getByte(pos);
                break;
            case 2:
                ret = this.buffer.getShort(pos);
                break;
            case 4:
                ret = this.buffer.getInt(pos);
                break;
            case 8:
                ret = this.buffer.getLong(pos);
                break;
            default:
                getRuntime().newArgumentError("offset must be 1, 2, 4 or 8");
        }
        return getRuntime().newFixnum(ret);
    }

    private int getIntVal(IRubyObject val) {
        return (int) val.convertToInteger().getLongValue();
    }
}
