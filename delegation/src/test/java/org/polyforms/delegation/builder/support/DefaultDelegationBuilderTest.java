package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.delegation.builder.DelegationBuilder;
import org.polyforms.delegation.builder.DelegationRegistry;
import org.polyforms.parameter.ArgumentProvider;

public class DefaultDelegationBuilderTest {
    private DelegationRegistry delegationRegistry;
    private DelegationBuilder delegationBuilder;

    @Before
    public void setUp() {
        delegationRegistry = EasyMock.createMock(DelegationRegistry.class);
        delegationBuilder = new DefaultDelegationBuilder(delegationRegistry);
    }

    @Test
    public void delegateAllToDomain() throws NoSuchMethodException {
        final Method delegatorMethod = DomainDelegator.class.getMethod("get", new Class<?>[] { DomainObject.class });
        delegationRegistry.contains(DomainDelegator.class, delegatorMethod);
        EasyMock.expectLastCall().andReturn(false);
        delegationRegistry.contains(DomainDelegator.class,
                DomainDelegator.class.getMethod("set", new Class<?>[] { DomainObject.class, String.class }));
        EasyMock.expectLastCall().andReturn(true);
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, delegatorMethod));
        EasyMock.replay(delegationRegistry);

        delegationBuilder.delegateFrom(DomainDelegator.class);
        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void delegateAllToDomainWithOverride() throws NoSuchMethodException {
        final Method getMethod = DomainDelegator.class.getMethod("get", new Class<?>[] { DomainObject.class });
        final Method setMethod = DomainDelegator.class.getMethod("set", new Class<?>[] { DomainObject.class,
                String.class });
        delegationRegistry.contains(DomainDelegator.class, getMethod);
        EasyMock.expectLastCall().andReturn(false);
        delegationRegistry.contains(DomainDelegator.class, setMethod);
        EasyMock.expectLastCall().andReturn(true);
        final ArgumentProvider argumentProvider = EasyMock.createMock(ArgumentProvider.class);
        argumentProvider.validate(setMethod);
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, getMethod));
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, setMethod));
        EasyMock.replay(delegationRegistry, argumentProvider);

        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        Assert.assertNull(delegationBuilder.delegate());
        domainDelegator.set(null, null);
        final DomainObject domainObject = delegationBuilder.delegate();
        delegationBuilder.parameter(argumentProvider);
        domainObject.set(null);
        delegationBuilder.map(RuntimeException.class, Exception.class);
        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry, argumentProvider);
    }

    @Test
    public void delegateToBean() throws NoSuchMethodException {
        final Method delegatorMethod = DomainDelegator.class.getMethod("get", new Class<?>[] { DomainObject.class });
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, delegatorMethod));
        EasyMock.replay(delegationRegistry);

        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        delegationBuilder.delegateTo(DomainObject.class);
        delegationBuilder.withName("domainObject");
        Assert.assertEquals(0, domainDelegator.get(null));
        Assert.assertNotNull(delegationBuilder.delegate());
        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void delegateWithParameterMatching() throws NoSuchMethodException {
        final Method delegatorMethod = DomainDelegator.class.getMethod("parameterMatch", new Class<?>[] {
                DomainObject.class, int.class, String.class });
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, delegatorMethod));
        EasyMock.replay(delegationRegistry);

        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.parameterMatch(null, 0, null);
        final DomainObject domainObject = delegationBuilder.delegate();
        domainObject.join(null, 0);
        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void delegateWithDuplicateParameterTypes() throws NoSuchMethodException {
        final Method delegatorMethod = DomainDelegator.class.getMethod("parameterNotMatch", new Class<?>[] {
                DomainObject.class, String.class, String.class });
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, delegatorMethod));
        EasyMock.replay(delegationRegistry);

        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.parameterNotMatch(null, null, null);
        final DomainObject domainObject = delegationBuilder.delegate();
        domainObject.join(null, 0);
        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void delegateWithParameterNotMatching() throws NoSuchMethodException {
        final Method delegatorMethod = DomainDelegator.class.getMethod("parameterNotMatch", new Class<?>[] {
                DomainObject.class, long.class, String.class });
        delegationRegistry.register(new SimpleDelegation(DomainDelegator.class, delegatorMethod));
        EasyMock.replay(delegationRegistry);

        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.parameterNotMatch(null, 0, null);
        final DomainObject domainObject = delegationBuilder.delegate();
        domainObject.join(null, 0);
        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry);
    }

    @Test
    public void registerDelegationsWithoudDelegatorType() {
        EasyMock.replay(delegationRegistry);

        delegationBuilder.registerDelegations();
        EasyMock.verify(delegationRegistry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeDelegatorMethodTwice() {
        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.get(null);
        domainDelegator.get(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void invokeDelegateeMethodTwice() {
        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        Assert.assertEquals(0, domainDelegator.get(null));
        final DomainObject domainObject = delegationBuilder.<DomainObject> delegate();
        domainObject.get();
        domainObject.get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setParameterBeforeDelegate() {
        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.get(null);
        delegationBuilder.parameter(EasyMock.createMock(ArgumentProvider.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setParameterMoreThanRequired() {
        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.get(null);
        final DomainObject domainObject = delegationBuilder.<DomainObject> delegate();
        delegationBuilder.parameter(EasyMock.createMock(ArgumentProvider.class));
        delegationBuilder.parameter(EasyMock.createMock(ArgumentProvider.class));
        domainObject.get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void cannotFindMethodInDelegateeType() {
        final DomainDelegator domainDelegator = delegationBuilder.delegateFrom(DomainDelegator.class);
        domainDelegator.cannotFindMethodInDelegateeType(null);
        Assert.assertNotNull(delegationBuilder.delegate());
        delegationBuilder.registerDelegations();
    }

    @Test(expected = IllegalArgumentException.class)
    public void mapWithoudDelegatorType() {
        delegationBuilder.map(Exception.class, RuntimeException.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void delegateWithoudDelegatorType() {
        delegationBuilder.delegate();
    }

    public static abstract class DomainDelegator {
        public void concret() {
        }

        public abstract void noParameter();

        public abstract void cannotFindMethodInDelegateeType(DomainObject domainObject);

        public abstract int get(DomainObject domainObject);

        public abstract void set(DomainObject domainObject, String string);

        public abstract void parameterMatch(DomainObject domainObject, int order, String name);

        public abstract void parameterNotMatch(DomainObject domainObject, String order, String name);

        public abstract void parameterNotMatch(DomainObject domainObject, long order, String name);
    }

    public interface DomainObject {
        int get();

        void set(String string);

        void join(String name, int number);
    }
}
