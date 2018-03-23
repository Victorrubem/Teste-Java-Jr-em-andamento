package br.com.cabtecgti.prova.agenda.repositories;

import java.io.Serializable;
import java.util.Collection;

/**
 * <pre>
 * <code>
 * {
 *  "result":[],
 *  "totalCount": 50,
 *  "offset": 10,
 *  "limit": 10,
 *  "previous": "http://...?offset=0&limit=10",
 *  "next": "http://...?offset=20&limit=10",
 * }
 * </code>
 * </pre>
 *
 * @author Cabtec GTI
 *
 */
public class ResultList<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Collection<T> result;
    private int totalCount;
    private int offset;
    private int limit;

    public ResultList() {
        // noop
    }

    public ResultList(final Collection<T> result, final int totalCount, final int offset, final int limit) {
        super();
        this.result = result;
        this.totalCount = totalCount;
        this.offset = offset;
        this.limit = limit;
    }

    public Collection<T> getResult() {
        return result;
    }

    public void setResult(final Collection<T> result) {
        this.result = result;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

}