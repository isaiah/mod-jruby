package org.vertx.jruby.api_shim.core;

import org.jruby.Ruby;
import org.jruby.RubyClass;
import org.jruby.RubyModule;
import org.jruby.RubyObject;
import org.jruby.anno.JRubyMethod;
import org.jruby.runtime.Block;
import org.jruby.runtime.ObjectAllocator;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;

/**
 * Created by isaiah on 12/14/13.
 */
public class RubyRouteMatcher extends RubyObject {
    private RouteMatcher routeMatcher;

    public static void createRubyRouteMatchClass(Ruby runtime) {
        RubyModule vertxModule = runtime.defineModule("Vertx");
        RubyClass rmClass = vertxModule.defineClassUnder("RouteMatcher", runtime.getObject(), new ObjectAllocator() {
            @Override
            public IRubyObject allocate(Ruby ruby, RubyClass rubyClass) {
                return null;
            }
        });
        rmClass.defineAnnotatedMethods(RubyRouteMatcher.class);
    }

    public RubyRouteMatcher(Ruby ruby, RubyClass klazz) {
        super(ruby, klazz);
    }

    @JRubyMethod
    public IRubyObject input(ThreadContext context, IRubyObject request) {
        routeMatcher.handle(((RubyHttpServerRequest) request).httpServerRequest());
        return this;
    }

    @JRubyMethod
    public IRubyObject get(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.get(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject put(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.put(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject post(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.post(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject delete(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.delete(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject options(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.options(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject head(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.head(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject trace(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.trace(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject patch(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.patch(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject connect(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.connect(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod
    public IRubyObject all(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.all(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name="get_re")
    public IRubyObject getWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.getWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "put_re")
    public IRubyObject putWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.putWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "post_re")
    public IRubyObject postWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.postWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "delete_re")
    public IRubyObject deleteWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.deleteWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "options_re")
    public IRubyObject optionsWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.optionsWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "head_re")
    public IRubyObject headWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.headWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "trace_re")
    public IRubyObject traceWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.traceWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "patch_re")
    public IRubyObject patchWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.patchWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "connect_re")
    public IRubyObject connectWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.connectWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }

    @JRubyMethod(name = "all_re")
    public IRubyObject allWithRegEx(final ThreadContext context, IRubyObject pattern, final Block blk) {
        this.routeMatcher.allWithRegEx(pattern.asJavaString(), new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }


    @JRubyMethod(name = "no_match")
    public IRubyObject noMatch(final ThreadContext context, final Block blk) {
        this.routeMatcher.noMatch(new Handler<HttpServerRequest>() {
            @Override
            public void handle(HttpServerRequest httpServerRequest) {
                RubyHttpServerRequest req = JRubyUtils.newHttpServerRequest(context.runtime, httpServerRequest);
                blk.call(context, req);
            }
        });
        return this;
    }
}
