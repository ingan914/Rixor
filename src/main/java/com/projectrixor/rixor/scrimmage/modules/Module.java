package com.projectrixor.rixor.scrimmage.modules;

import com.projectrixor.rixor.scrimmage.Match;
import org.dom4j.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class Module
{
    private static Map<Class<? extends Module>, ModuleClass> globalModules = new HashMap();

    public ModuleClass getInfo()
    {
        return getInfo(getClass());
    }

    public String getName()
    {
        return getInfo().getName();
    }
    public ListenerModule createListenerModule(Match match) {
        return null;
    }
    public static Module parse(ModuleFactory context, Logger logger, Document doc) { return null; }


    public static ModuleClass getInfo(Class<? extends Module> module)
    {
        ModuleClass info = (ModuleClass)globalModules.get(module);
        if (info == null) {
            info = new ModuleClass(module);
            globalModules.put(module, info);
        }
        return info;
    }
}
