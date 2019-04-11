package org.hl7.fhir.dstu3.model;

import org.hl7.fhir.instance.model.api.IBase;

import java.util.*;

public class ResourceContainer implements IBase
{
    private Map<String, List<Resource>> resourceContainerMap = new HashMap<>();

    public ResourceContainer(List<Resource> resources)
    {
        for (Resource resource : resources)
        {
            if (resourceContainerMap.containsKey(resource.fhirType()))
            {
                resourceContainerMap.get(resource.fhirType()).add(resource);
            }
            else
            {
                List<Resource> containedList = new ArrayList<>();
                containedList.add(resource);
                resourceContainerMap.put(resource.fhirType(), containedList);
            }
        }
    }

    public List<Resource> getContainedResourcesOfType(String type)
    {
        if (resourceContainerMap.containsKey(type))
        {
            return resourceContainerMap.get(type);
        }

        return null;
    }

    @Override
    public boolean isEmpty() {
        return resourceContainerMap.isEmpty();
    }

    @Override
    public boolean hasFormatComment() {
        return false;
    }

    @Override
    public List<String> getFormatCommentsPre() {
        return null;
    }

    @Override
    public List<String> getFormatCommentsPost() {
        return null;
    }
}
