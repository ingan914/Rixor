package com.projectrixor.rixor.scrimmage.modules;

import java.lang.reflect.InvocationTargetException;
 import java.lang.reflect.Method;
 import java.util.HashSet;
 import java.util.Set;
 import java.util.logging.Logger;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import org.dom4j.Document;

public class ModuleFactoryContext
 {
   private final Scrimmage scrimmage;
   private final Logger log;
   private final Set<ModuleInfo> modules = new HashSet();

   public ModuleFactoryContext(Scrimmage scrimmage, Logger log)
   {
     this.scrimmage = scrimmage;
     this.log = log;
   }

   public Scrimmage getScrimmage() {
     return this.scrimmage;
   }

   public Logger getLogger() {
     return this.log;
   }

   public void register(Class<? extends Module> moduleClass)
   {
     this.modules.add(Module.getInfo(moduleClass));
   }

   public ModuleInfo getInfo(String name)
   {
     for (ModuleInfo info : this.modules) {
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

   public Module create(ModuleContext context, ModuleInfo info, Document doc)
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
