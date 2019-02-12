package io.flexio.services.api.documentation;

import io.flexio.services.api.documentation.RessourcesManager.FileSystemRessourcesManager;
import io.flexio.services.api.documentation.handlers.*;
import io.flexio.services.support.api.Api;
import org.codingmatters.poom.services.support.Env;
import org.codingmatters.rest.api.Processor;
import com.fasterxml.jackson.core.JsonFactory;
import io.flexio.services.api.documentation.api.FlexioApiDocumentationHandlers;
import io.flexio.services.api.documentation.service.FlexioApiDocumentationProcessor;

public class FlexioApiDocumentationApi implements Api {

    private final String name = "flexio-api-documentation";
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

    @Override
    public Processor processor() {
        return this.processor;
    }

    @Override
    public String docResource() {
        return "flexio-api-documentation.html";
    }

    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String version() {
        return "v2";
    }
}

