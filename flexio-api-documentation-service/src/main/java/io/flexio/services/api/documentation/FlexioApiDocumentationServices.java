package io.flexio.services.api.documentation;

import io.undertow.Undertow;
import org.codingmatters.poom.services.logging.CategorizedLogger;
import org.codingmatters.poom.services.support.Env;
import org.codingmatters.rest.undertow.CdmHttpUndertowHandler;

public class FlexioApiDocumentationServices {
    private static CategorizedLogger log = CategorizedLogger.getLogger(FlexioApiDocumentationServices.class);

    public static void main(String[] args){
        String host = Env.mandatory(Env.SERVICE_HOST).asString();
        int port = Env.mandatory(Env.SERVICE_PORT).asInteger();
        FlexioApiDocumentationServices service = new FlexioApiDocumentationServices(port, host, api());
        service.start();
    }

    private Undertow server;
    private final int port;
    private final String host;
    private final FlexioApiDocumentationApi api;

    public FlexioApiDocumentationServices(int port, String host, FlexioApiDocumentationApi api) {
        this.port = port;
        this.host = host;
        this.api = api;
    }

    static public FlexioApiDocumentationApi api(){
        return new FlexioApiDocumentationApi();
    }


    public void start() {
        this.server = Undertow.builder()
                .addHttpListener(this.port, this.host)
                .setHandler(new CdmHttpUndertowHandler(this.api.processor()))
                .build();
        this.server.start();
    }

    
}

