package org.vertx.jruby.api_shim.core;

import org.jruby.*;
import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyNumeric;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.ObjectAllocator;
import static org.jruby.runtime.Visibility.*;
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
        bufferClass.defineAnnotatedMethods(RubyBuffer.class);
    }

    private RubyBuffer(Ruby ruby, RubyClass rubyClass) {
        super(ruby, rubyClass);
    }

    public void setBuffer(Buffer buff) {
        this.buffer = buff;
    }

    public Buffer getBuffer() {
        return this.buffer;
    }

    @JRubyMethod(name="initialize", visibility = PRIVATE)
    public IRubyObject initialize(ThreadContext context, IRubyObject initialSizeHint) {
        this.buffer = new Buffer(RubyNumeric.num2int(initialSizeHint));
        return this;
    }

    @JRubyMethod(meta=true)
    public IRubyObject create(ThreadContext context, IRubyObject recv, IRubyObject initialSizeHint) {
        RubyBuffer buff = (RubyBuffer) ((RubyClass) recv).allocate();
        buff.buffer = new Buffer(RubyNumeric.num2int(initialSizeHint));
        return buff;
    }

    @JRubyMethod(name="create_from_str", required=1, optional=1, meta=true)
    public IRubyObject createFromStr(ThreadContext context, IRubyObject recv, IRubyObject[] args) {
        RubyBuffer buff = (RubyBuffer) ((RubyClass) recv).allocate();
        buff.buffer = new Buffer(args[0].asJavaString(), args.length > 1 ? args[1].asJavaString() : "UTF-8");
        return buff;
    }

    @JRubyMethod(name="to_s", optional=1)
    public IRubyObject toString(ThreadContext context, IRubyObject arg) {
        return getRuntime().newString(this.buffer.toString(arg.isNil() ? "UTF-8" : arg.asJavaString()));
    }

    @JRubyMethod(name="get_byte")
    public IRubyObject getByte(ThreadContext context, IRubyObject position) {
        return getRuntime().newFixnum(this.buffer.getByte(RubyNumeric.num2int(position)));
    }

    @JRubyMethod(name="get_fixnum")
    public IRubyObject getFixnum(ThreadContext context, IRubyObject position, IRubyObject bytes) {
        long ret = 0;
        int pos = RubyNumeric.num2int(position);
        switch(RubyNumeric.num2int(bytes)) {
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
                throw getRuntime().newArgumentError("bytes must be 1, 2, 4 or 8");
        }
        return getRuntime().newFixnum(ret);
    }

    @JRubyMethod(name="get_float")
    public IRubyObject getFloat(ThreadContext context, IRubyObject position, IRubyObject bytes) {
        double ret = 0;
        int pos = RubyNumeric.num2int(position);
        switch(RubyNumeric.num2int(bytes)) {
            case 4:
                ret = this.buffer.getFloat(pos);
                break;
            case 8:
                ret = this.buffer.getDouble(pos);
                break;
            default:
                throw getRuntime().newArgumentError("bytes must be 4 or 8");
        }
        return getRuntime().newFloat(ret);
    }


    @JRubyMethod(name="get_string", required=2, optional = 1)
    public IRubyObject getString(ThreadContext context, IRubyObject[] args) {
        return context.runtime.newString(this.buffer.getString(RubyNumeric.num2int(args[0]), RubyNumeric.num2int(args[1]), args.length > 2 ? args[2].asJavaString() : "UTF-8"));
    }

    @JRubyMethod(name="get_buffer")
    public IRubyObject getBuffer(ThreadContext context, IRubyObject pos, IRubyObject endPos) {
        Buffer buffer = this.buffer.getBuffer(RubyNumeric.num2int(pos), RubyNumeric.num2int(endPos));
        RubyBuffer buff = (RubyBuffer) this.getType().allocate();
        buff.buffer = buffer;
        return buff;
    }

    @JRubyMethod(name="append_buffer")
    public IRubyObject appendBuffer(ThreadContext context, IRubyObject buff) {
        this.buffer.appendBuffer(((RubyBuffer) buff).buffer);
        return this;
    }


    @JRubyMethod(name="append_fixnum")
    public IRubyObject appendFixnum(ThreadContext context, IRubyObject num, IRubyObject bytes) {
        switch (RubyNumeric.num2int(bytes)) {
            case 1:
                this.buffer.appendByte(RubyNumeric.num2chr(num));
                break;
            case 2:
                this.buffer.appendShort((short) RubyNumeric.num2int(num));
                break;
            case 4:
                this.buffer.appendInt(RubyNumeric.num2int(num));
                break;
            case 8:
                this.buffer.appendLong(RubyNumeric.num2long(num));
                break;
            default:
                throw context.runtime.newArgumentError("bytes must be 1, 2, 4 or 8");
        }
        return this;
    }


    @JRubyMethod(name="append_float")
    public IRubyObject appendFloat(ThreadContext context, IRubyObject num, IRubyObject bytes) {
        switch (RubyNumeric.num2int(bytes)) {
            case 4:
                this.buffer.appendFloat((float)RubyNumeric.num2dbl(num));
                break;
            case 8:
                this.buffer.appendDouble(RubyNumeric.num2dbl(num));
                break;
            default:
                throw context.runtime.newArgumentError("bytes must be 4 or 8");

        }
        return this;
    }

    @JRubyMethod(name="append_str", required=1, optional=1)
    public IRubyObject appendStr(ThreadContext context, IRubyObject[] args) {
        this.buffer.appendString(args[0].asJavaString(), args.length > 1 ? args[1].asJavaString() : "UTF-8");
        return this;
    }

    @JRubyMethod(name="set_fixnum")
    public IRubyObject setFixnum(ThreadContext context, IRubyObject pos, IRubyObject num, IRubyObject bytes) {
        int position = RubyNumeric.num2int(pos);
        switch (RubyNumeric.num2int(bytes)) {
            case 1:
                this.buffer.setByte(position, RubyNumeric.num2chr(num));
                break;
            case 2:
                this.buffer.setShort(position, (short) RubyNumeric.num2int(num));
                break;
            case 4:
                this.buffer.setInt(position, RubyNumeric.num2int(num));
                break;
            case 8:
                this.buffer.setLong(position, RubyNumeric.num2long(num));
                break;
            default:
                throw context.runtime.newArgumentError("bytes must be 1, 2, 4 or 8");
        }
        return this;
    }

    @JRubyMethod(name="set_float")
    public IRubyObject setFloat(ThreadContext context, IRubyObject pos, IRubyObject num, IRubyObject bytes) {
        int position = RubyNumeric.num2int(pos);
        switch (RubyNumeric.num2int(bytes)) {
            case 4:
                this.buffer.setFloat(position, (float) RubyNumeric.num2dbl(num));
                break;
            case 8:
                this.buffer.setDouble(position, RubyNumeric.num2dbl(num));
                break;
            default:
                throw getRuntime().newArgumentError("bytes must be 4 or 8");
        }
        return this;
    }

    @JRubyMethod(name="set_buffer")
    public IRubyObject setBuffer(ThreadContext context, IRubyObject pos, IRubyObject buff) {
        this.buffer.setBytes(RubyNumeric.num2int(pos), ((RubyBuffer) buff).buffer.getBytes());
        return this;
    }

    /*
     * Set bytes in the buffer to the string encoding in the specified encoding
     * @param pos [FixNum] - the position in this buffer from where to start writing the string
     * @param str [String] the string
     * @param enc [String] the encoding
     * @return [Buffer] a reference to self so multiple operations can be appended together.
     */
    @JRubyMethod(name="set_string", required=2, optional=1)
    public IRubyObject setString(ThreadContext context, IRubyObject[] args) {
        this.buffer.setString(RubyNumeric.num2int(args[0]), args[1].asJavaString(), args.length > 2 ? args[2].asJavaString() : "UTF-8");
        return this;
    }

    /*
     * @return [FixNum] the length of this buffer, in bytes.
     */
    @JRubyMethod
    public IRubyObject length(ThreadContext context) {
        return getRuntime().newFixnum(this.buffer.length());
    }
}
