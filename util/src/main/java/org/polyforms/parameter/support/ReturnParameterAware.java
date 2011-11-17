package org.polyforms.parameter.support;

import org.polyforms.parameter.Parameter;
import org.polyforms.parameter.Parameters;

/**
 * Aggregate class of {@link Parameter}s with return value.
 * 
 * @author Kuisong Tong
 * @since 1.0
 * 
 * @param <P> type of parameter
 */
public interface ReturnParameterAware<P extends Parameter> extends Parameters<P> {
    /**
     * Get parameter of return value.
     * 
     * @return null if return type is void.
     */
    P getReturnParameter();
}
