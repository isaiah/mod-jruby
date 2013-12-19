package org.vertx.jruby.api_shim;

import org.jruby.RubyObject;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

/**
 * Created by isaiah on 12/19/13.
 */
public interface RubySSLSupport<T extends RubyObject> {
    T setSSL(ThreadContext context, IRubyObject ssl);

    IRubyObject isSSL(ThreadContext context);

    T setKeyStorePath(ThreadContext context, IRubyObject path);

    IRubyObject keyStorePath(ThreadContext context);

    T setKeyStorePassword(ThreadContext context, IRubyObject password);

    IRubyObject keyStorePassword(ThreadContext context);

    T setTrustStorePath(ThreadContext context, IRubyObject path);

    IRubyObject trustStorePath(ThreadContext context);

    T setTrustStorePassword(ThreadContext context, IRubyObject password);

    IRubyObject trustStorePassword(ThreadContext context);

}
