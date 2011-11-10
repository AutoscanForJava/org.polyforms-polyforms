package org.polyforms.parameter.provider;

import java.lang.reflect.Method;

import org.polyforms.parameter.ArgumentProvider;
import org.springframework.util.Assert;

/**
 * Argument Resolved by type of arguments which are used to invoke method.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public final class ArgumentOfType implements ArgumentProvider {
    private final Class<?> type;
    private int position = -1;

    /**
     * Create an instance with type of parameter.
     */
    public ArgumentOfType(final Class<?> type) {
        Assert.notNull(type);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    public Object get(final Object... arguments) {
        Assert.isTrue(position >= 0, "Please invoke method validate before get.");
        return arguments[position];
    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Method method) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            final Class<?> parameterType = parameterTypes[i];
            if (type.equals(parameterType)) {
                Assert.isTrue(position < 0, "There is more than one parameter of type " + type
                        + " in delegator method.");
                position = i;
            }
        }
        Assert.isTrue(position >= 0, "There is no parameter of type " + type + " in delegator method.");
    }
}
