package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.ResourceNotFoundException;
import io.flexio.services.api.documentation.ResourcesManager.ResourcesManager;
import io.flexio.services.api.documentation.api.GroupsGetRequest;
import io.flexio.services.api.documentation.api.GroupsGetResponse;
import io.flexio.services.api.documentation.api.groupsgetresponse.Status200;
import io.flexio.services.api.documentation.api.groupsgetresponse.Status500;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Group;
import org.codingmatters.poom.services.logging.CategorizedLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GetGroups implements Function<GroupsGetRequest, GroupsGetResponse> {
    private ResourcesManager fs;
    private static CategorizedLogger log = CategorizedLogger.getLogger(GetGroups.class);


    public GetGroups(ResourcesManager fs) {
        this.fs = fs;
    }

    @Override
    public GroupsGetResponse apply(GroupsGetRequest groupsGetRequest) {
        try {
            List<String> groups = this.fs.getGroups();

            List<Group> listGroups = new ArrayList<Group>();
            for (String group : groups) {
                listGroups.add(Group.builder().name(group).build());
            }

            log.info("GroupsGetRequest normal use");

            return GroupsGetResponse.builder().status200(
                    Status200.builder().payload(listGroups).build()
            ).build();
        } catch (ResourceNotFoundException e) {
            return GroupsGetResponse.builder().status500(
                    Status500.builder().payload(
                            Error.builder()
                                    .token(log.tokenized().info("Dir not exists, Deleted ?", e))
                                    .code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }
    }
}
