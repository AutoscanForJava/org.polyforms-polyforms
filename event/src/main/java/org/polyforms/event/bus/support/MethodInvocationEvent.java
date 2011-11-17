package org.polyforms.event.bus.support;

import java.lang.reflect.Method;

import org.polyforms.event.bus.Event;
import org.polyforms.util.ArrayUtils;
import org.springframework.util.Assert;

/**
 * An event published from method invocation.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public class MethodInvocationEvent extends Event {
    private final Class<?> targetClass;
    private final Method method;
    private final Object[] arguments;

    /**
     * Create an instance with method invocation information.
     */
    public MethodInvocationEvent(final String name, final Class<?> targetClass, final Method method,
            final Object... arguments) {
        super(name);
        Assert.notNull(targetClass);
        Assert.notNull(method);

        this.targetClass = targetClass;
        this.method = method;
        this.arguments = arguments;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Method getMethod() {
        return method;
    }

    /**
     * Get copy of arguments.
     */
    public Object[] getArguments() {
        return ArrayUtils.clone(arguments);
    }
}
