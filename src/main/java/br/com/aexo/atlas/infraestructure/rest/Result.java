package br.com.aexo.atlas.infraestructure.rest;

import java.util.Iterator;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Result {

	
	
	public <T extends ResultView<T>> T use(Class<T> type) {
		return (T) lookup(type);
	}
	
	private BeanManager getBeanManager() {
        try {
            return (BeanManager) InitialContext.doLookup("java:comp/BeanManager");
        }
        catch (final NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T lookup(final Class<T> clazz) {
        final BeanManager bm = getBeanManager();
        final Iterator<Bean<?>> iter = bm.getBeans(clazz).iterator();
        if (!iter.hasNext()) {
            throw new IllegalStateException("CDI BeanManager cannot find an instance of requested type " + clazz.getName());
        }
        final Bean<T> bean = (Bean<T>) iter.next();
        final CreationalContext<T> ctx = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, clazz, ctx);
    }

}
