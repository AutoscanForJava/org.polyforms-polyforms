package org.polyforms.repository.spring;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.polyforms.repository.aop.RepositoryInterceptor;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            final Method specificMethod = getMostSpecificMethod(method, targetClass);
            if (!Modifier.isAbstract(specificMethod.getModifiers())) {
                LOGGER.trace("Skip concret method {}.", specificMethod);
                return false;
            }

            return repositoryMatcher.matches(targetClass);
        }

        private Method getMostSpecificMethod(final Method method, final Class<?> targetClass) {
            final Class<?> clazz = AopUtils.isCglibProxyClass(targetClass) ? targetClass.getSuperclass() : targetClass;
            return AopUtils.getMostSpecificMethod(method, clazz);
        }
    }
}
