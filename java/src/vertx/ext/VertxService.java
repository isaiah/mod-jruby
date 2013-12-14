package vertx.ext;

import org.jruby.Ruby;
import org.jruby.runtime.load.BasicLibraryService;
import org.vertx.jruby.api_shim.core.RubyBuffer;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: isaiah
 * Date: 13/12/2013
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class VertxService implements BasicLibraryService {
    public boolean basicLoad(final Ruby runtime) throws IOException {
        RubyBuffer.createBufferClass(runtime);

        return true;
    }
}
