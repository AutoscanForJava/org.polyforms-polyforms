package org.polyforms.repository.jpa.executor;

import java.lang.reflect.Method;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryBuilder.QueryType;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Implementation of method which returns count of matching entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class CountBy extends QueryExecutor {
    /**
     * Create an instance with {@link EntityClassResolver} and {@link QueryBuilder}.
     */
    @Inject
    public CountBy(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
            final QueryParameterBinder queryParameterBinder) {
        super(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    @Override
    protected Object getResult(final Method method, final Query query) {
        return query.getSingleResult();
    }

    @Override
    protected QueryType getQueryType() {
        return QueryType.COUNT;
    }
}
