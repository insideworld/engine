package insideworld.engine.web.quarkus.beans;

import com.google.common.collect.ImmutableList;
import insideworld.engine.datatransfer.endpoint.PreExecute;
import insideworld.engine.quarkus.AbstractBeans;
import insideworld.engine.web.TagHandler;
import java.util.Collection;
import java.util.Collections;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.ws.rs.core.HttpHeaders;


public class WebBeans extends AbstractBeans {

    @Produces
    public Collection<TagHandler> tagHandlers(final Instance<TagHandler> instance) {
        return this.fromInstance(instance);
    }

    @Produces
    public Collection<PreExecute<HttpHeaders>> httpHeadersPre(final Instance<PreExecute<HttpHeaders>> instance) {
        return this.fromInstance(instance);
    }



}
