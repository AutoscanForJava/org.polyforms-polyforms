package org.polyforms.delegation.builder;

/**
 * Internal interface is used to build delegation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationBuilder {
    /**
     * Set delegator type.
     * 
     * @param delegatorType
     * @return proxy of delegator type
     */
    <S> S delegateFrom(Class<S> delegatorType);

    /**
     * Set delegatee type.
     * 
     * @param delegateeType
     */
    void delegateTo(Class<?> delegateeType);

    /**
     * Set delegatee name.
     * 
     * @param name of delegatee
     */
    void withName(String name);

    /**
     * Create a delegation.
     * 
     * @return proxy of delegator
     */
    <T> T delegate();

    /**
     * Add parameter provider.
     * 
     * @param parameterProvider
     */
    void parameter(ParameterProvider<?> parameterProvider);

    /**
     * Map exception type.
     * 
     * @param sourceType
     * @param targetType
     */
    void map(Class<? extends Throwable> sourceType, Class<? extends Throwable> targetType);

    /**
     * Register delegations into registry.
     */
    void registerDelegations();
}
