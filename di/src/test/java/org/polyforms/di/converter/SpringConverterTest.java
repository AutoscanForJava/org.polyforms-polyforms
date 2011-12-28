package org.polyforms.di.converter;

import java.lang.annotation.ElementType;
import java.lang.reflect.Method;
import java.util.Locale;

import javax.inject.Provider;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.ConditionalConverter.MatchResult;
import org.modelmapper.spi.MappingContext;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.convert.support.GenericConversionService;

public class SpringConverterTest {
    private Provider<ConversionService> provider;
    private ConditionalConverter<Object, Object> converter;
    private MappingContext<Object, Object> context;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        provider = EasyMock.createMock(Provider.class);
        converter = new SpringConverter(provider);
        context = EasyMock.createMock(MappingContext.class);
    }

    @Test
    public void convertNull() {
        context.getSource();
        EasyMock.expectLastCall().andReturn(null);
        EasyMock.replay(context);

        Assert.assertNull(converter.convert(context));
        EasyMock.verify(context);
    }

    @Test
    public void convert() {
        final ConversionService conversionService = EasyMock.createMock(ConversionService.class);

        context.getSource();
        EasyMock.expectLastCall().andReturn("METHOD");
        provider.get();
        EasyMock.expectLastCall().andReturn(conversionService);
        context.getSourceType();
        EasyMock.expectLastCall().andReturn(String.class);
        context.getDestinationType();
        EasyMock.expectLastCall().andReturn(ElementType.class);
        conversionService.convert("METHOD", TypeDescriptor.valueOf(String.class),
                TypeDescriptor.valueOf(ElementType.class));
        EasyMock.expectLastCall().andReturn(ElementType.METHOD);
        EasyMock.replay(context, conversionService, provider);

        Assert.assertSame(ElementType.METHOD, converter.convert(context));
        EasyMock.verify(context, conversionService, provider);
    }

    @Test
    public void supports() {
        prepareConversionService();

        Assert.assertSame(MatchResult.FULL, converter.match(String.class, Locale.class));
        EasyMock.verify(provider);
    }

    @Test
    public void notSupports() {
        prepareConversionService();

        Assert.assertSame(MatchResult.NONE, converter.match(String.class, Method.class));
        EasyMock.verify(provider);
    }

    private void prepareConversionService() {
        final GenericConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new ModelMapperConverter(EasyMock.createMock(ModelMapper.class)));

        provider.get();
        EasyMock.expectLastCall().andReturn(conversionService);
        EasyMock.replay(provider);
    }
}
