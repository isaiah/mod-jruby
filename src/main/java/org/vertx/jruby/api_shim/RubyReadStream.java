package org.vertx.jruby.api_shim;

import org.jruby.RubyObject;
import org.jruby.runtime.Block;
import org.jruby.runtime.ThreadContext;

/**
 * Created by isaiah on 12/19/13.
 */
public interface RubyReadStream<T extends RubyObject> {
    T dataHandler(final ThreadContext context, final Block blk);

    T pause(ThreadContext context);

    T resumt(ThreadContext context);

    T exceptionHandler(final ThreadContext context, final Block blk);

    T endHandler(final ThreadContext context, final Block blk);
}
