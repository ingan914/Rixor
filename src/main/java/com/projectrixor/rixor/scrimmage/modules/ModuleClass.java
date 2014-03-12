package com.projectrixor.rixor.scrimmage.modules;

import java.util.HashSet;
import java.util.Set;

public class ModuleClass
{
    private final Class<? extends Module> moduleClass;
    private final ModuleAnnotation desc;

    public ModuleClass(Class<? extends Module> moduleClass)
    {
        this.moduleClass = moduleClass;

        this.desc = ((ModuleAnnotation)moduleClass.getAnnotation(ModuleAnnotation.class));
        if (this.desc == null)
            throw new IllegalStateException("Module must have a annotation!");
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
