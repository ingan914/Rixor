package com.projectrixor.rixor.scrimmage.modules;

 import java.util.HashMap;
 import java.util.Map;
 import java.util.logging.Logger;
 import com.projectrixor.rixor.scrimmage.Match;
 import org.dom4j.Document;

 public abstract class Module
 {
   private static Map<Class<? extends Module>, ModuleInfo> globalModules = new HashMap();

   public ModuleInfo getInfo()
   {
     return getInfo(getClass());
   }

   public String getName()
   {
     return getInfo().getName();
   }
   public ListenerModule createListener(Match match) {
     return null;
   }
   public static Module parse(ModuleContext context, Logger logger, Document doc) { return null; }


   public static ModuleInfo getInfo(Class<? extends Module> module)
   {
     ModuleInfo info = (ModuleInfo)globalModules.get(module);
    if (info == null) {
       info = new ModuleInfo(module);
       globalModules.put(module, info);
     }
     return info;
   }
 }