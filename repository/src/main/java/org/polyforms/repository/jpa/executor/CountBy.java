package org.polyforms.repository.jpa.executor;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.polyforms.repository.jpa.QueryBuilder;
import org.polyforms.repository.jpa.QueryParameterBinder;
import org.polyforms.repository.spi.EntityClassResolver;

/**
 * Implementation of find method which returns count of matching entities.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class CountBy extends QueryExecutor {
    /**
     * Create an instance with {@link EntityClassResolver}.
     */
    @Inject
    public CountBy(final EntityClassResolver entityClassResolver, final QueryBuilder queryBuilder,
            final QueryParameterBinder queryParameterBinder) {
        super(entityClassResolver, queryBuilder, queryParameterBinder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object getResult(final Query query) {
        return query.getSingleResult();
    }
}
