// http://www.verborgh.be/articles/2010/01/06/porting-the-viewscoped-jsf-annotation-to-cdi/
package br.com.cabtecgti.faces.bean.cdi;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.cabtecgti.faces.bean.ViewScoped;

public class ViewContext implements Context {

    private static final Logger logger = LoggerFactory.getLogger(ViewContext.class);
    private static final String MAP_ID = "gradeti.fwk.faces.bean.cdi.ViewContext.MAP_ID";
    private static final String FLAG = "gradeti.fwk.faces.bean.cdi.ViewContext";

    private boolean registrouListener;

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(final Contextual<T> contextual) {

        final Bean<T> bean = (Bean<T>) contextual;
        final Map<String, Object> viewMap = getViewMap();

        if (viewMap.containsKey(bean.getName())) {
            return (T) viewMap.get(bean.getName());
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(final Contextual<T> contextual, final CreationalContext<T> creationalContext) {

        final FacesContext fctx = FacesContext.getCurrentInstance();
        final UIViewRoot viewRoot = fctx.getViewRoot();
        final Map<String, Object> viewMap = viewRoot.getViewMap(true);

        final Bean<T> bean = (Bean<T>) contextual;

        if (viewMap.containsKey(bean.getName())) {
            return (T) viewMap.get(bean.getName());
        } else {
            final T t = bean.create(creationalContext);
            viewMap.put(bean.getName(), t);

            registrarCreationalContext(viewMap, bean, creationalContext);

            return t;
        }
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return ViewScoped.class;
    }

    @Override
    public boolean isActive() {

        return getViewMap() != null;
    }

    private Map<String, Object> getViewMap() {

        final FacesContext fctx = FacesContext.getCurrentInstance();
        final UIViewRoot viewRoot = fctx.getViewRoot();
        if (viewRoot != null) {
            return viewRoot.getViewMap(true);
        } else {
            return null;
        }
    }

    protected void registrarCreationalContext(final Map<String, Object> viewMap,
                                              final Bean<?> contextual,
                                              final CreationalContext<?> creationalContext) {

        final FacesContext fctx = FacesContext.getCurrentInstance();
        final Object session = fctx.getExternalContext().getSession(true);

        if (session != null) {
            final Map<String, Object> sessionMap = fctx.getExternalContext().getSessionMap();

            if (sessionMap.get(FLAG) == null) {
                sessionMap.put(FLAG, new HashMap<>(2));
            }

            @SuppressWarnings("unchecked")
            final Map<String, Map<String, Object[]>> viewContextMap =
                (Map<String, Map<String, Object[]>>) sessionMap.get(FLAG);

            String viewMapId = (String) viewMap.get(MAP_ID);

            if (isBlank(viewMapId)) {
                synchronized (viewContextMap) {
                    viewMapId = UUID.randomUUID().toString();
                    while (viewContextMap.containsKey(viewMapId)) {
                        viewMapId = UUID.randomUUID().toString();
                    }
                    viewContextMap.put(viewMapId, new HashMap<String, Object[]>(2));
                    viewMap.put(MAP_ID, viewMapId);
                }
            }

            final Map<String, Object[]> map = viewContextMap.get(viewMapId);
            map.put(contextual.getName(), new Object[] {contextual, creationalContext});
        }

        if (!registrouListener) {
            registrouListener = true;
            fctx.getApplication().subscribeToEvent(PreDestroyViewMapEvent.class,
                new DestroyViewScopeEventListener());
        }
    }

    // Inner classes

    static class DestroyViewScopeEventListener implements SystemEventListener {

        @SuppressWarnings("unchecked")
        @Override
        public void processEvent(final SystemEvent event) {

            final FacesContext fctx = FacesContext.getCurrentInstance();
            final Object session = fctx.getExternalContext().getSession(false);

            if (session != null) {
                final Map<String, Object> sessionMap = fctx.getExternalContext().getSessionMap();
                if (sessionMap.get(FLAG) == null) {
                    return;
                }
                
                final Map<String, Object> viewMap = ((UIViewRoot) event.getSource()).getViewMap(false);

                final Map<String, Map<String, Object[]>> viewContextMap =
                    (Map<String, Map<String, Object[]>>) sessionMap.get(FLAG);

                final String viewMapId = (String) viewMap.get(MAP_ID);
                
                if (isNotBlank(viewMapId)) {
                    
                    final Map<String, Object[]> map = viewContextMap.get(viewMapId);
                    for (final Entry<String, Object[]> entry : map.entrySet()) {
                        final Object obj = viewMap.get(entry.getKey());
                        
                        @SuppressWarnings("rawtypes")
                        final Contextual contextual = (Contextual) entry.getValue()[0];
                        final CreationalContext<?> creationalContext = (CreationalContext<?>) entry.getValue()[1];
                        
                        try {
                            contextual.destroy(obj, creationalContext);
                        } catch (final Exception e) {
                            logger.warn("DestroyScopeEventListener - erro ao destruir: {}", e, e);
                        }
                    }
                }
            }
            
        }

        @Override
        public boolean isListenerForSource(final Object source) {
            return source instanceof UIViewRoot;
        }
    };

    private static boolean isBlank(final String str) {
        return str == null || str.trim().isEmpty();
    }

    private static boolean isNotBlank(final String str) {
        return !isBlank(str);
    }
}