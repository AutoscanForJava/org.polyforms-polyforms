package org.polyforms.repository.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.polyforms.repository.aop.RepositoryInterceptor;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.polyforms.util.AopUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

/**
 * {@link org.springframework.aop.Advisor} for methods which are in repository.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Component
public final class RepositoryAdvisor extends DefaultPointcutAdvisor {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryAdvisor.class);
    private static final long serialVersionUID = 618599462955606986L;

    /**
     * Create an instance with {@link RepositoryInterceptor}.
     */
    @Autowired
    public RepositoryAdvisor(final RepositoryInterceptor repositoryInterceptor,
            final RepositoryMatcher repositoryMatcher) {
        super(new RepositoryMatcherPointcut(repositoryMatcher), repositoryInterceptor);
    }

    private static final class RepositoryMatcherPointcut extends StaticMethodMatcherPointcut {
        private final RepositoryMatcher repositoryMatcher;

        protected RepositoryMatcherPointcut(final RepositoryMatcher repositoryMatcher) {
            super();
            this.repositoryMatcher = repositoryMatcher;
        }

        /**
         * {@inheritDoc}
         */
        public boolean matches(final Method method, final Class<?> targetClass) {
            final Class<?> clazz = deProxy(targetClass, method);
            final Method specificMethod = ClassUtils.getMostSpecificMethod(method, clazz);
            if (!Modifier.isAbstract(specificMethod.getModifiers())) {
                LOGGER.trace("Skip concret method {}.", specificMethod);
                return false;
            }

            final boolean matches = repositoryMatcher.matches(clazz);
            if (matches) {
                LOGGER.trace("Found repository {}.", clazz);
            }
            return matches;
        }

        private Class<?> deProxy(final Class<?> targetClass, final Method method) {
            for (final Class<?> clazz : AopUtils.deproxy(targetClass)) {
                if (ReflectionUtils.findMethod(clazz, method.getName(), method.getParameterTypes()) != null) {
                    return clazz;
                }
            }
            return targetClass;
        }
    }
}
