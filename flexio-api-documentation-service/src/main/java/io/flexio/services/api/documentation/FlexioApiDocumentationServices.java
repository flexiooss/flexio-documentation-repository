package io.flexio.services.api.documentation;

import com.fasterxml.jackson.core.JsonFactory;
import io.undertow.Undertow;
import org.codingmatters.poom.ci.pipeline.api.service.PoomCIApi;
import org.codingmatters.poom.ci.pipeline.api.service.PoomCIPipelineService;
import org.codingmatters.poom.services.support.Env;
import org.codingmatters.rest.undertow.CdmHttpUndertowHandler;

import java.io.File;

public class FlexioApiDocumentationServices {

    private Undertow server;
    private final int port;
    private final String host;
    private final FlexioApiDocumentationApi api;

    public FlexioApiDocumentationServices(int port, String host, FlexioApiDocumentationApi api) {
        this.port = port;
        this.host = host;
        this.api = api;
    }

    public static void main(String[] args){
        String host = Env.mandatory(Env.SERVICE_HOST).asString();
        int port = Env.mandatory(Env.SERVICE_PORT).asInteger();
       // TODO start service
        FlexioApiDocumentationServices service = new FlexioApiDocumentationServices(port, host, api());
        service.start();
    }

    static public FlexioApiDocumentationApi api(){
        JsonFactory jsonFactory = new JsonFactory();

        File logStorage = new File(Env.mandatory("LOG_STORAGE").asString());
        String jobRegistryUrl = Env.mandatory("JOB_REGISTRY_URL").asString();

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

