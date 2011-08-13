package org.polyforms.repository.converter;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.core.convert.TypeDescriptor;

/**
 * Converter which converts entity to its identifier.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class EntityToIdentifierConverter extends EntityConverter {
    /**
     * Create an instance with {@link EntityHelper}.
     */
    @Inject
    public EntityToIdentifierConverter(final EntityHelper entityHelper) {
        super(entityHelper);
    }

    /**
     * {@inheritDoc}
     */
    public boolean matches(final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        return canBeConverted(sourceType.getType(), targetType.getType());
    }

    /**
     * {@inheritDoc}
     */
    public Object convert(final Object source, final TypeDescriptor sourceType, final TypeDescriptor targetType) {
        if (source == null) {
            return null;
        }
        return getEntityHelper().getIdentifierValue(source);
    }
}
