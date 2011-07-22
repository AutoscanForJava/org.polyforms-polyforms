package org.polyforms.di.spring.schema;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.test.util.ReflectionTestUtils;

public class PolyformsNamespaceHandlerTest {
    @Test
    @SuppressWarnings("unchecked")
    public void registerBeanDefinitionParser() {
        final PolyformsNamespaceHandler handler = new PolyformsNamespaceHandler();
        handler.init();

        final Map<String, BeanDefinitionParser> parsers = (Map<String, BeanDefinitionParser>) ReflectionTestUtils
                .getField(handler, "parsers");
        Assert.assertEquals(1, parsers.size());
        Assert.assertTrue(parsers.get("component-scan") instanceof PolyformsComponentScanBeanDefinitionParser);
    }
}
