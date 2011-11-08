package org.polyforms.parameter.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.polyforms.parameter.Parameters;
import org.polyforms.parameter.annotation.Provider;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.ClassUtils;

public class MethodParameters implements Parameters<MethodParameter> {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    private final MethodParameter[] parameters;

    public MethodParameters(final Class<?> clazz, final Method method, final boolean applyAnnotation) {
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        parameters = new MethodParameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            final MethodParameter parameter = new MethodParameter();
            parameters[i] = parameter;

            parameter.setIndex(i);

            final Class<?> type = ClassUtils.resolvePrimitiveIfNecessary(GenericTypeResolver.resolveParameterType(
                    new org.springframework.core.MethodParameter(method, i), clazz));
            parameter.setType(type);

            if (parameterNames != null) {
                parameter.setName(parameterNames[i]);
            }

            parameter.setAnnotation(getFirstProviderAnnotation(parameterAnnotations[i]), applyAnnotation);
        }
    }

    private Annotation getFirstProviderAnnotation(final Annotation[] annotations) {
        for (final Annotation annotation : annotations) {
            if (annotation.getClass().isAnnotationPresent(Provider.class)) {
                return annotation;
            }
        }

        return null;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }
}
