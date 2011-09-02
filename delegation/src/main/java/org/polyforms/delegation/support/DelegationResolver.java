package org.polyforms.delegation.support;

import java.lang.reflect.Method;

import org.polyforms.delegation.builder.Delegation;

/**
 * Strategy of resolving {@link Delegation} built for specific {@link Delegator}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
interface DelegationResolver {
    /**
     * Retrieve delegation related with specified delegator.
     * 
     * @param delegator
     * @return related delegation or <code>null</code> if not exist.
     */
    Delegation get(Delegator delegator);

    /**
     * Check whether a delegation for specific method supports.
     * 
     * @param delegator the delegator
     * @return true if there is a delegation of specific delegator, false if not
     */
    boolean supports(Delegator delegator);
}

final class Delegator {
    private final Class<?> type;
    private final Method method;

    protected Delegator(final Class<?> type, final Method method) {
        this.type = type;
        this.method = method;
    }

    protected Class<?> getType() {
        return type;
    }

    protected Method getMethod() {
        return method;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type.hashCode();
        result = prime * result + method.hashCode();
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Delegator)) {
            return false;
        }

        final Delegator other = (Delegator) obj;

        return type == other.type && method.equals(other.method);
    }
}
