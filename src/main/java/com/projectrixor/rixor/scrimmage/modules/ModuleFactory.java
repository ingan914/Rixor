package com.projectrixor.rixor.scrimmage.modules;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import org.dom4j.Document;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class ModuleFactory
{
    private final Scrimmage scrimmage;
    private final Logger log;
    private final Set<ModuleClass> modules = new HashSet();

    public ModuleFactory(Scrimmage scrimmage, Logger log)
    {
        this.scrimmage = scrimmage;
        this.log = log;
    }

    public Scrimmage getScrim() {
        return this.scrimmage;
    }

    public Logger getLogger() {
        return this.log;
    }

    public void register(Class<? extends Module> moduleClass)
    {
        this.modules.add(Module.getInfo(moduleClass));
    }

    public ModuleClass getInfo(String name)
    {
        for (ModuleClass info : this.modules) {
            if (info.getName().equalsIgnoreCase(name)) {
                return info;
            }
        }
        return null;
    }

    public Set<ModuleInfo> getModules()
    {
        return this.modules;
    }

    public Module create(ModuleContext context, ModuleClass info, Document doc)
    {
        try
        {
            Method parser = info.getModuleClass().getMethod("parse", new Class[] { ModuleContext.class, Logger.class, Document.class });
            return (Module)parser.invoke(null, new Object[] { context, this.log, doc });
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }return null;
    }
}
