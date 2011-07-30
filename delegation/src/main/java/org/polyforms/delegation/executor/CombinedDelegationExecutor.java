package org.polyforms.delegation.executor;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.polyforms.delegation.builder.DelegationRegistry.Delegation;
import org.polyforms.delegation.support.DelegationExecutor;
import org.polyforms.di.BeanContainer;
import org.springframework.core.convert.ConversionService;

/**
 * Factory which selects executor to execute specific {@link Delegation}.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
@Named
public final class CombinedDelegationExecutor implements DelegationExecutor {
    private final Map<Delegation, DelegationExecutor> delegationExecutorMapping = new HashMap<Delegation, DelegationExecutor>();
    private final BeanContainer beanContainer;
    private final DelegationExecutor beanExecutor;
    private final DelegationExecutor domainExecutor;

    /**
     * Create an instance with {@link ConversionService} and {@link BeanContainer}.
     */
    @Inject
    public CombinedDelegationExecutor(final ConversionService conversionService, final BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
        beanExecutor = new BeanDelegationExecutor(conversionService, beanContainer);
        domainExecutor = new DomainDelegationExecutor(conversionService);
    }

    /**
     * {@inheritDoc}
     */
    public Object execute(final Delegation delegation, final Class<?> delegatorClass, final Object[] arguments)
            throws Throwable {
        return getDelegationExecutor(delegation).execute(delegation, delegatorClass, arguments);
    }

    private DelegationExecutor getDelegationExecutor(final Delegation delegation) {
        if (!delegationExecutorMapping.containsKey(delegation)) {
            delegationExecutorMapping.put(delegation, isBeanDelegation(delegation) ? beanExecutor : domainExecutor);
        }
        return delegationExecutorMapping.get(delegation);
    }

    private boolean isBeanDelegation(final Delegation delegation) {
        return delegation.hasName() || beanContainer.containsBean(delegation.getDelegatee().getDeclaringClass());
    }
}
