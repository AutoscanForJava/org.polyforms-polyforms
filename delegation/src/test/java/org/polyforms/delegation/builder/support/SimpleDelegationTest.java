package org.polyforms.delegation.builder.support;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Test;
import org.polyforms.parameter.provider.ArgumentProvider;

public class SimpleDelegationTest {
    private final SimpleDelegation delegationA;
    private final SimpleDelegation delegationB;

    public SimpleDelegationTest() throws NoSuchMethodException {
        delegationA = new SimpleDelegation(String.class, String.class.getMethod("toString", new Class<?>[0]));
        delegationB = new SimpleDelegation(String.class, String.class.getMethod("hashCode", new Class<?>[0]));
    }

    @Test
    public void getDelegatorType() throws NoSuchMethodException {
        Assert.assertSame(String.class, delegationA.getDelegatorType());
    }

    @Test
    public void getDelegatorMethod() throws NoSuchMethodException {
        Assert.assertEquals(String.class.getMethod("toString", new Class<?>[0]), delegationA.getDelegatorMethod());
    }

    @Test
    public void getDelegateeType() throws NoSuchMethodException {
        delegationA.setDelegateeType(Integer.class);
        Assert.assertSame(Integer.class, delegationA.getDelegateeType());
    }

    @Test
    public void getDelegateeName() throws NoSuchMethodException {
        delegationA.setDelegateeName("delegatee");
        Assert.assertSame("delegatee", delegationA.getDelegateeName());
    }

    @Test
    public void getDelegateeMethod() throws NoSuchMethodException {
        final Method hashCodeMethod = String.class.getMethod("hashCode", new Class<?>[0]);
        delegationA.setDelegateeMethod(hashCodeMethod);
        Assert.assertEquals(hashCodeMethod, delegationA.getDelegateeMethod());
    }

    @Test
    public void getargumentProviders() {
        final ArgumentProvider argumentProvider = EasyMock.createMock(ArgumentProvider.class);
        delegationA.addArgumentProvider(argumentProvider);
        final List<ArgumentProvider> argumentProviders = delegationA.getArgumentProviders();
        Assert.assertEquals(1, argumentProviders.size());
        Assert.assertSame(argumentProvider, argumentProviders.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unmodifiedargumentProviders() {
        final ArgumentProvider argumentProvider = EasyMock.createMock(ArgumentProvider.class);
        delegationA.getArgumentProviders().add(argumentProvider);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getExceptionType() {
        final Map<Class<? extends Throwable>, Class<? extends Throwable>> exceptionTypeMap = EasyMock
                .createMock(Map.class);
        exceptionTypeMap.get(Exception.class);
        EasyMock.expectLastCall().andReturn(RuntimeException.class);
        EasyMock.replay(exceptionTypeMap);

        delegationA.setExceptionTypeMap(exceptionTypeMap);
        Assert.assertEquals(RuntimeException.class, delegationA.getExceptionType(Exception.class));
        EasyMock.verify(exceptionTypeMap);
    }

    @Test
    public void getExceptionTypeWithNullMap() {
        Assert.assertNull(delegationA.getExceptionType(Exception.class));
    }

    @Test
    public void hashcode() {
        Assert.assertTrue(delegationA.hashCode() != delegationB.hashCode());
    }

    @Test
    public void equalsSame() {
        Assert.assertTrue(delegationA.equals(delegationA));
    }

    @Test
    public void notEqualsNull() {
        Assert.assertFalse(delegationA.equals(null));
    }

    @Test
    public void notEqualsOtherClass() {
        Assert.assertFalse(delegationA.equals(new Object()));
    }

    @Test
    public void notEqualsType() throws NoSuchMethodException {
        Assert.assertFalse(delegationA.equals(new SimpleDelegation(Object.class, Object.class.getMethod("toString",
                new Class<?>[0]))));
    }

    @Test
    public void notEqualsMethod() {
        Assert.assertFalse(delegationA.equals(delegationB));
    }

    @Test
    public void equals() throws NoSuchMethodException {
        Assert.assertTrue(delegationA.equals(new SimpleDelegation(String.class, String.class.getMethod("toString",
                new Class<?>[0]))));
    }
}
