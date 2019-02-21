package io.flexio.services.api.documentation;

import com.fasterxml.jackson.core.JsonFactory;
import io.flexio.services.api.documentation.ResourcesManager.FileSystemResourcesManager;
import io.flexio.services.api.documentation.api.FlexioApiDocumentationDescriptor;
import io.flexio.services.api.documentation.api.FlexioApiDocumentationHandlers;
import io.flexio.services.api.documentation.handlers.*;
import io.flexio.services.api.documentation.service.FlexioApiDocumentationProcessor;
import org.codingmatters.poom.services.support.Env;
import org.codingmatters.rest.api.Processor;

public class FlexioApiDocumentationApi {

    private FlexioApiDocumentationHandlers handlers;
    private FlexioApiDocumentationProcessor processor;

    public FlexioApiDocumentationApi(){
        String storageDir = Env.optional("STORAGE_DIR")
                .orElse(new Env.Var(System.getProperty("java.io.tmpdir"))).asString();

        String manifestDir = Env.optional("MANIFEST_DIR")
                .orElse(new Env.Var(System.getProperty("java.io.tmpdir"))).asString();

        String tmpDir = Env.optional("TMP_DIR")
                .orElse(new Env.Var(System.getProperty("java.io.tmpdir"))).asString();

        FileSystemResourcesManager fs = new FileSystemResourcesManager(storageDir, manifestDir, tmpDir);

        this.handlers = new FlexioApiDocumentationHandlers.Builder()
                .filePostHandler(new CreateClassifer(fs) )
                .fileGetHandler(new GetResources(fs))
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
        return "/" + FlexioApiDocumentationDescriptor.NAME;
    }
}
