package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.RessourceNotFoundException;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.ClassifiersGetRequest;
import io.flexio.services.api.documentation.api.ClassifiersGetResponse;
import io.flexio.services.api.documentation.api.classifiersgetresponse.Status200;
import io.flexio.services.api.documentation.api.classifiersgetresponse.Status400;
import io.flexio.services.api.documentation.api.classifiersgetresponse.Status404;
import io.flexio.services.api.documentation.api.classifiersgetresponse.Status500;
import io.flexio.services.api.documentation.api.types.Classifier;
import io.flexio.services.api.documentation.api.types.Error;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GetClassifiers implements Function<ClassifiersGetRequest, ClassifiersGetResponse> {
    private RessourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(GetClassifiers.class);


    public GetClassifiers(RessourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public ClassifiersGetResponse apply(ClassifiersGetRequest classifiersGetRequest) {
        if (classifiersGetRequest.opt().group().orElse("").isEmpty() ||
        classifiersGetRequest.opt().module().orElse("").isEmpty() ||
        classifiersGetRequest.opt().version().orElse("").isEmpty()){
            return ClassifiersGetResponse.builder().status400(
                    Status400.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("Lack of parameter.s", classifiersGetRequest))
                                    .code(Error.Code.INCOMPLETE_REQUEST).build()
                    ).build()
            ).build();
        }

        try {
            List<String> list = this.fs.getClassifiers(classifiersGetRequest.group(),
                    classifiersGetRequest.module(),
                    classifiersGetRequest.version());

            List<Classifier> listClassifier = new ArrayList<Classifier>();
            for (String name : list) {
                listClassifier.add(Classifier.builder().name(name).build());
            }

            return ClassifiersGetResponse.builder().status200(
                    Status200.builder().payload(listClassifier).build())
                    .build();
        }catch (RessourceNotFoundException e){
            return ClassifiersGetResponse.builder().status404(
                    Status404.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("Ressource not found"))
                                    .code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }
    }
}
