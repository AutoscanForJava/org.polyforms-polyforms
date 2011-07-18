package org.polyforms.repository.spring;

import junit.framework.Assert;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.aop.RepositoryInterceptor;
import org.polyforms.repository.spi.ExecutorFinder;
import org.polyforms.repository.spi.RepositoryMatcher;
import org.springframework.aop.MethodMatcher;

public class RepositoryAdvisorTest {
    private RepositoryMatcher repositoryMatcher;
    private MethodMatcher methodMatcher;

    @Before
    public void setUp() {
        repositoryMatcher = EasyMock.createMock(RepositoryMatcher.class);
        final RepositoryInterceptor repositoryInterceptor = new RepositoryInterceptor(
                EasyMock.createMock(ExecutorFinder.class));
        methodMatcher = new RepositoryAdvisor(repositoryInterceptor, repositoryMatcher).getPointcut()
                .getMethodMatcher();
    }

    @Test
    public void matches() throws NoSuchMethodException {
        repositoryMatcher.matches(MockClass.class);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(repositoryMatcher);

        Assert.assertTrue(methodMatcher.matches(MockClass.class.getMethod("abstractMethod", new Class<?>[0]),
                MockClass.class));
        EasyMock.verify(repositoryMatcher);
    }

    @Test
    public void matchesProxy() throws NoSuchMethodException {
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(MockClass.class);
        enhancer.setCallbacks(new Callback[] { EasyMock.createMock(MethodInterceptor.class) });
        final Class<?> proxyClass = enhancer.create().getClass();

        repositoryMatcher.matches(proxyClass);
        EasyMock.expectLastCall().andReturn(true);
        EasyMock.replay(repositoryMatcher);

        Assert.assertTrue(methodMatcher.matches(MockClass.class.getMethod("abstractMethod", new Class<?>[0]),
                proxyClass));
        EasyMock.verify(repositoryMatcher);
    }

    @Test
    public void notMatchesConcret() throws NoSuchMethodException {
        Assert.assertFalse(methodMatcher.matches(MockClass.class.getMethod("concretMethod", new Class<?>[0]),
                MockClass.class));
    }

    public static abstract class MockClass {
        public void concretMethod() {
        }

        public abstract void abstractMethod();
    }
}
