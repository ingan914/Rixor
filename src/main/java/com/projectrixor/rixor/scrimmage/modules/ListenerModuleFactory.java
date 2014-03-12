package com.projectrixor.rixor.scrimmage.modules;

import java.util.Set;

public class ListenerModuleFactory
{
    protected final Set<ListenerModule> listeners;

    public ListenerModuleFactory(Set<ListenerModule> listeners)
    {
        this.listeners = listeners;
    }

    public <T extends ListenerModule> T getListener(Class<T> listenerModuleClass)
    {
        for (ListenerModule listenerModule : this.listeners) {
            if (listenerModuleClass.isInstance(listenerModule)) {
                return (T)listenerModule;
            }
        }
        return null;
    }

    public void enable() {
        for (ListenerModule listenerModule : this.listeners)
            listenerModule.enable();
    }

    public void disable()
    {
        for (ListenerModule listenerModule : this.listeners)
            listenerModule.disable();
    }
}
