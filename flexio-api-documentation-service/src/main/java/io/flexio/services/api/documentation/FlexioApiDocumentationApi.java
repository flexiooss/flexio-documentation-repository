package io.flexio.services.api.documentation;

import io.flexio.services.api.documentation.RessourcesManager.FileSystemRessourcesManager;
import io.flexio.services.api.documentation.handlers.*;
import org.codingmatters.poom.services.support.Env;
import org.codingmatters.rest.api.Processor;
import com.fasterxml.jackson.core.JsonFactory;
import io.flexio.services.api.documentation.api.FlexioApiDocumentationHandlers;
import io.flexio.services.api.documentation.service.FlexioApiDocumentationProcessor;

public class FlexioApiDocumentationApi {

    private final String name = "flexio-api-documentation";

    public FlexioApiDocumentationApi(String apiPath) {
        this.apiPath = apiPath;
    }

    private String apiPath;
    private FlexioApiDocumentationHandlers handlers;
    private FlexioApiDocumentationProcessor processor;

    public FlexioApiDocumentationApi(){
        String storageDir = Env.optional("STORAGE_DIR")
                .orElse(new Env.Var(System.getProperty("java.io.tmpdir"))).asString();

        FileSystemRessourcesManager fs = new FileSystemRessourcesManager(storageDir);

        this.handlers = new FlexioApiDocumentationHandlers.Builder()
                .filePostHandler(new CreateClassifer(fs) )
                .fileGetHandler(new GetRessources(fs))
                .classifiersGetHandler(new GetClassifiers(fs))
                .groupsGetHandler(new GetGroups(fs))
                .modulesGetHandler(new GetModules(fs))
                .versionsGetHandler(new GetVersions(fs))
                .build();

        this.processor = new FlexioApiDocumentationProcessor(this.path(), new JsonFactory(), handlers);
    }

    public Processor processor() {
        return this.processor;
    }

    public FlexioApiDocumentationHandlers getHandlers() {
        return this.handlers;
    }

    public String path() {
        return apiPath;
    }
}
