package com.projectrixor.rixor.scrimmage.modules;

 import java.util.ArrayList;
import java.util.HashSet;
 import java.util.List;
import java.util.Set;

 public class ModuleInfo
 {
   private final Class<? extends Module> moduleClass;
   private final ModuleDescription desc;

   public ModuleInfo(Class<? extends Module> moduleClass)
   {
     this.moduleClass = moduleClass;

     this.desc = ((ModuleDescription)moduleClass.getAnnotation(ModuleDescription.class));
     if (this.desc == null)
       throw new IllegalStateException("module must be decorated with a ModuleDescription");
   }

   public Class<? extends Module> getModuleClass()
   {
     return this.moduleClass;
   }

   public String getName()
   {
      return this.desc.name();
   }

   public Set<ModuleInfo> getDepends()
   {
     Set infos = new HashSet(this.desc.depends().length);

            for (Class module : this.desc.depends()) {
       infos.add(Module.getInfo(module));
     }
     return infos;
   }
 }

/* Location:           C:\Users\Maxim\Desktop\PGM-2.2.0-SNAPSHOT.jar
 * Qualified Name:     tc.oc.pgm.modules.ModuleInfo
 * JD-Core Version:    0.6.2
 */