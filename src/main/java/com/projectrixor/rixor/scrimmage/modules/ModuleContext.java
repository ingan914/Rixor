  package com.projectrixor.rixor.scrimmage.modules;

  import com.projectrixor.rixor.scrimmage.Match;
  import com.projectrixor.rixor.scrimmage.countdowns.CountdownFactory;
  import com.sun.scenario.effect.FilterContext;

  import java.util.HashSet;
  import java.util.Set;


  public class ModuleContext
  {
    private final CountdownFactory countdownFactory;
    private final RegionContext regionContext;
    private final FilterContext filterContext;
   protected final Set<Module> modules = new HashSet();

    public ModuleContext(ModuleFactoryContext factory, CountdownFactory countdownFactory, RegionContext regionContext, FilterContext filterContext, Document doc) {
     this.countdownFactory = countdownFactory;
     this.regionContext = regionContext;
     this.filterContext = filterContext;

     for (ModuleInfo info : factory.getModules())
       loadModule(factory, doc, info);
    }

    private boolean loadModule(ModuleFactoryContext factory, Document doc, ModuleInfo info)
    {
     if (hasModule(info)) {
       return true;
      }

     for (ModuleInfo depend : info.getDepends()) {
        if (!loadModule(factory, doc, depend)) {
         return false;
        }

      }

     Module newModule = factory.create(this, info, doc);
     if (newModule == null) {
       return false;
      }
     this.modules.add(newModule);
     return true;
    }

    public ListenerModuleFactory createListener(Match match)
    {
     Set listenerModules = new HashSet();
     for (Module module : this.modules) {
       ListenerModule listenerModule = module.createListener(match);
       if (listenerModule != null) {
           listenerModule.add(listenerModule);
        }
      }
     return new ListenerModuleFactory(listenerModules);
    }

    public CountdownFactory getCountdownFactory() {
     return this.countdownFactory;
    }

    public RegionContext getRegionContext() {
     return this.regionContext;
    }

    public FilterContext getFilterContext() {
     return this.filterContext;
    }

    public Set<Module> getModules() {
     return this.modules;
    }

    public boolean hasModule(ModuleInfo info) {
     return getModule(info) != null;
    }

    public boolean hasModule(Class<? extends Module> moduleClass) {
     return getModule(Module.getInfo(moduleClass)) != null;
    }

    public Module getModule(ModuleInfo info) {
     for (Module module : this.modules) {
       if (module.getInfo() == info) {
         return module;
        }
      }
     return null;
    }

    public <T extends Module> T getModule(Class<T> moduleClass)
    {
     return (T)getModule(Module.getInfo(moduleClass));
    }
  }