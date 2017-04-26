package org.opencds.cqf.cql.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 4/26/2017.
 */
public class AliasList {
    private List base;
    private String name;

    public AliasList(String name) {
        this.name = name;
        this.base = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getBase() {
        return this.base;
    }

    public void setBase(List base) {
        this.base = base;
    }

    public AliasList withBase(List base) {
        setBase(base);
        return this;
    }
}
