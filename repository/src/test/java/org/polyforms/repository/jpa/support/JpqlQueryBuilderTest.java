package org.polyforms.repository.jpa.support;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.polyforms.repository.ExecutorPrefix;
import org.polyforms.repository.jpa.EntityHelper;
import org.springframework.test.util.ReflectionTestUtils;

public class JpqlQueryBuilderTest {
    private JpqlQueryBuilder queryBuilder;
    private ExecutorPrefix executorPrefix;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        executorPrefix = EasyMock.createMock(ExecutorPrefix.class);
        queryBuilder = new JpqlQueryBuilder(executorPrefix, EasyMock.createMock(EntityHelper.class));
        entityManager = EasyMock.createMock(EntityManager.class);
        ReflectionTestUtils.setField(queryBuilder, "entityManager", entityManager);
    }

    @Test
    public void build() throws NoSuchMethodException {
        final String queryString = "DELETE FROM MockEntity e WHERE e.name = ?1 ";
        final Query query = EasyMock.createMock(Query.class);

        executorPrefix.convertToPrefix("Delete");
        EasyMock.expectLastCall().andReturn("delete");
        executorPrefix.getPrefix("Delete");
        EasyMock.expectLastCall().andReturn(Collections.singleton("remove"));
        entityManager.createQuery(queryString);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(executorPrefix, entityManager);

        Assert.assertSame(
                query,
                queryBuilder.build("Delete", MockEntity.class,
                        MockRepository.class.getMethod("deleteByName", new Class<?>[] { String.class })));
        EasyMock.verify(executorPrefix, entityManager);
    }

    @Test
    public void buildByDefaultBuilder() throws NoSuchMethodException {
        final String queryString = "SELECT e FROM MockEntity e WHERE e.name = ?1 ";
        final Query query = EasyMock.createMock(Query.class);

        executorPrefix.convertToPrefix("FindBy");
        EasyMock.expectLastCall().andReturn("find");
        executorPrefix.getPrefix("FindBy");
        EasyMock.expectLastCall().andReturn(Collections.singleton("find"));
        entityManager.createQuery(queryString);
        EasyMock.expectLastCall().andReturn(query);
        EasyMock.replay(executorPrefix, entityManager);

        Assert.assertSame(
                query,
                queryBuilder.build("FindBy", MockEntity.class,
                        MockRepository.class.getMethod("findByName", new Class<?>[] { String.class })));
        EasyMock.verify(executorPrefix, entityManager);
    }

    private interface MockRepository {
        List<Object> findByName(String name);

        void deleteByName(String name);
    }

    @SuppressWarnings("unused")
    private static class MockEntity {
        private String name;
    }
}
