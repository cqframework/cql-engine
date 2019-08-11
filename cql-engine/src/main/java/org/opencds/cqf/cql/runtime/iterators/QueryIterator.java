package org.opencds.cqf.cql.runtime.iterators;

import javafx.util.Pair;
import org.opencds.cqf.cql.elm.execution.QueryEvaluator;
import org.opencds.cqf.cql.execution.Context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Bryn on 8/11/2019.
 */
public class QueryIterator implements Iterator<Object> {

    private Context context;
    private List<Iterator> sources;
    private Iterator<Object> sourceIterator;
    private ArrayList<Object> result;

    public QueryIterator(Context context, List<Iterator> sources) {
        this.context = context;
        this.sources = sources;
        this.result = new ArrayList(sources.size());

        for (int i = sources.size() - 1; i >= 0; i--) {
            if (sourceIterator == null) {
                sourceIterator = sources.get(i);
            }
            else {
                sourceIterator = new TimesIterator(sources.get(i), sourceIterator);
            }
            result.add(null);
        }
    }

    @Override
    public boolean hasNext() {
        return sourceIterator.hasNext();
    }

    @Override
    public Object next() {
        return unpack(sourceIterator.next());
    }

    private Object unpack(Object element) {
        unpair(element, result, 0);
        return result;
    }

    private void unpair(Object element, List<Object> target, int index) {
        if (element instanceof Pair) {
            unpair(((Pair)element).getKey(), target, index);
            unpair(((Pair)element).getValue(), target, index + 1);
        }
        else {
            target.set(index, element);
        }
    }
}

