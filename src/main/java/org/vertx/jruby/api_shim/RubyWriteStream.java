package org.vertx.jruby.api_shim;

import org.jruby.RubyObject;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.jruby.api_shim.core.RubyBuffer;

/**
 * Created by isaiah on 12/19/13.
 */
public interface RubyWriteStream<T extends RubyObject> {
    T write(ThreadContext context, RubyBuffer buffer);

    IRubyObject setWriteQueueMaxSize(ThreadContext context, IRubyObject size);

    IRubyObject writeQueueMaxSize(ThreadContext context);

    IRubyObject isWriteQueueFull(ThreadContext context);

    IRubyObject drainHandler(final ThreadContext context, final Block blk);

    IRubyObject exceptionHandler(final ThreadContext context, final Block blk);
}
