package io.flexio.services.api.documentation.handlers;

import io.flexio.services.api.documentation.Exceptions.DirectoryNotExistsException;
import io.flexio.services.api.documentation.RessourcesManager.FileSystemRessourcesManager;
import io.flexio.services.api.documentation.RessourcesManager.RessourcesManager;
import io.flexio.services.api.documentation.api.GroupsGetRequest;
import io.flexio.services.api.documentation.api.GroupsGetResponse;
import io.flexio.services.api.documentation.api.groupsgetresponse.Status200;
import io.flexio.services.api.documentation.api.groupsgetresponse.Status404;
import io.flexio.services.api.documentation.api.groupsgetresponse.Status500;
import io.flexio.services.api.documentation.api.types.Error;
import io.flexio.services.api.documentation.api.types.Group;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GetGroups implements Function<GroupsGetRequest, GroupsGetResponse> {
    private RessourcesManager fs;

    public GetGroups(RessourcesManager fs){
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


            return GroupsGetResponse.builder().status200(
                    Status200.builder().payload(listGroups).build()
            ).build();
        }catch (DirectoryNotExistsException e){
            return GroupsGetResponse.builder().status500(
                    Status500.builder().payload(
                            Error.builder().code(Error.Code.RESOURCE_NOT_FOUND).build()
                    ).build()
            ).build();
        }catch (Exception e){
            return  GroupsGetResponse.builder().status500(
                    Status500.builder().build()
            ).build();
        }
    }
}