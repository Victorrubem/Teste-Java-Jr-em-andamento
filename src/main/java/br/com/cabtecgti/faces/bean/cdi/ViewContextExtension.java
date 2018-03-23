package br.com.cabtecgti.faces.bean.cdi;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;

public class ViewContextExtension implements Extension {

	public void afterBeanDiscovery(@Observes final AfterBeanDiscovery event,
			final BeanManager manager) {

		event.addContext(new ViewContext());
	}
	
}