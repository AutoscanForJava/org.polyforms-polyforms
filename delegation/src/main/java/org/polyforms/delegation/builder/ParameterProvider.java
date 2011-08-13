package org.polyforms.delegation.builder;

import java.util.ArrayList;
import java.util.List;

public interface ParameterProvider<P> {
    void validate(Class<?>... parameterTypes);

    P get(Object... arguments);

    public static final class At<P> implements ParameterProvider<P> {
        private final int position;

        public At(final int position) {
            if (position < 0) {
                throw new IllegalArgumentException("Parameter position(int) must start from 0.");
            }
            this.position = position;
        }

        @SuppressWarnings("unchecked")
        public P get(final Object... arguments) {
            return (P) arguments[position];
        }

        public void validate(final Class<?>... parameterType) {
            if (position >= parameterType.length) {
                throw new IllegalArgumentException("Parameter position " + position
                        + " must not less than parameter count " + parameterType.length + " of delegator method.");
            }
        }
    }

    public static final class Constant<P> implements ParameterProvider<P> {
        private final P value;

        public Constant(final P value) {
            this.value = value;
        }

        public P get(final Object... arguments) {
            return value;
        }

        /**
         * {@inheritDoc}
         */
        public void validate(final Class<?>... parameterType) {
        }
    }

    public static final class TypeOf<P> implements ParameterProvider<P> {
        private final Class<?> type;

        public TypeOf(final Class<?> type) {
            if (type == null) {
                throw new IllegalArgumentException("Parameter type (Class<P) must not be null.");
            }
            this.type = type;
        }

        @SuppressWarnings("unchecked")
        public P get(final Object... arguments) {
            for (final Object argument : arguments) {
                if (argument != null && type.isInstance(argument)) {
                    return (P) argument;
                }
            }

            return null;
        }

        public void validate(final Class<?>... parameterTypes) {
            final List<Class<?>> matchedParameterTypes = new ArrayList<Class<?>>();
            for (final Class<?> parameterType : parameterTypes) {
                if (type == parameterType) {
                    matchedParameterTypes.add(parameterType);
                }
            }

            if (matchedParameterTypes.size() != 1) {
                throw new IllegalArgumentException("There is one and only one parameter of type " + type
                        + " allowed in delegator method.");
            }
        }
    }
}
