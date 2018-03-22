package org.opencds.cqf.cql.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christopher on 4/26/2017.
 */
public class AliasList {
    private List<Object> base;
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

    public List<Object> getBase() {
        return this.base;
    }

    public void setBase(List<Object> base) {
        this.base = base;
    }

    public AliasList withBase(List<Object> base) {
        setBase(base);
        return this;
    }
}
