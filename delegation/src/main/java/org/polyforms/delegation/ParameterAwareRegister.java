package org.polyforms.delegation;

import org.polyforms.delegation.builder.DelegationBuilderHolder;
import org.polyforms.delegation.builder.DelegationRegister;
import org.polyforms.delegation.builder.ParameterProvider;
import org.polyforms.delegation.builder.ParameterProvider.At;
import org.polyforms.delegation.builder.ParameterProvider.Constant;
import org.polyforms.delegation.builder.ParameterProvider.TypeOf;
import org.polyforms.delegation.util.DefaultValue;

class ParameterAwareRegister<S> implements DelegationRegister<S> {
    /**
     * {@inheritDoc}
     */
    public void register(final S source) {
    }

    protected void map(final Class<? extends Throwable> sourceType, final Class<? extends Throwable> targetType) {
        DelegationBuilderHolder.get().map(sourceType, targetType);
    }

    protected final <P> P at(final Class<P> targetType, final int position) { // NOPMD
        return provideBy(targetType, new At<P>(position));
    }

    protected final <P> P typeOf(final Class<P> targetType, final Class<?> sourceType) {
        return provideBy(targetType, new TypeOf<P>(sourceType));
    }

    @SuppressWarnings("unchecked")
    protected final <P> P constant(final P value) {
        return provideBy((Class<P>) (value == null ? null : value.getClass()), new Constant<P>(value));
    }

    protected final <P> P provideBy(final Class<P> type, final ParameterProvider<P> parameterProvider) {
        DelegationBuilderHolder.get().parameter(parameterProvider);
        return DefaultValue.get(type);
    }
}
