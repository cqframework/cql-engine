package org.opencds.cqf.cql.execution;

/**
 * Created by Darren on 2018 Mar 9.
 */
public class TestDefinitionSourceMeta
{
    public enum SourceKind { JAVA_RESOURCE, FILESYSTEM }

    private SourceKind sourceKind;
    private Class hostClass; // when JAVA_RESOURCE
    private String dirPath;
    private String fileName;
    private String groupName;

    public TestDefinitionSourceMeta(SourceKind sourceKind,
        Class hostClass, String dirPath, String fileName, String groupName)
    {
        this.sourceKind = sourceKind;
        this.hostClass = hostClass;
        this.dirPath = dirPath;
        this.fileName = fileName;
        this.groupName = groupName;
    }

    public SourceKind getSourceKind()
    {
        return sourceKind;
    }

    public Class getHostClass()
    {
        return hostClass;
    }

    public String getDirPath()
    {
        return dirPath;
    }

    public String getFileName()
    {
        return fileName;
    }

    public String getGroupName()
    {
        return groupName;
    }
}
