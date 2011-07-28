package org.polyforms.delegation;

import java.lang.reflect.Method;

/**
 * The service interface for delegation. This is the entry point into the delegation system. Call
 * {@link #delegate(Method, Object[])} to invoke a related method which linked by builder.
 * 
 * The real method executed by invocation of the delegator is a method in a bean or in first parameter, which is binded
 * by delegation builder.
 * 
 * The {@link IllegalArgumentException} should be thrown if there is no bean with specified name in Ioc container or no
 * parameters while using first parameter as delegated target.
 * 
 * Parameters including all from client's invocation or might excepting first parameter while using it as delegated
 * would be passed to real method invocation in order.
 * 
 * if parameters passed are more than required, the more parameters should be ignored silently. In opposition, The
 * {@link IllegalArgumentException} should be thrown if less parameters.
 * 
 * Conversion might exist if type of passed and required are unmatching. Please check conversion service for more
 * detail.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface DelegationService {
    /**
     * Check if a method can be delegated to another method.
     * 
     * @param delegator the delegated method
     * @return true if the delegation can be performed, false if not
     */
    boolean canDelegate(Method delegator);

    /**
     * Delegate a invocation of method. Conversion might exist if parameters and/or return value don't match between
     * delegator and delegatee.
     * 
     * @param delegator the delegated method
     * @param arguments arguments for delegation
     * @return the return value of execution of delegation
     * 
     * @throws Throwable if invocation of delegatee method throw an exception
     */
    Object delegate(Object target, Method delegator, Object... arguments) throws Throwable;
}
