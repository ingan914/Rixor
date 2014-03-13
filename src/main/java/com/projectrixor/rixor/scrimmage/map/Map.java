package com.projectrixor.rixor.scrimmage.map;

import java.io.File;
import java.io.IOException;

import com.projectrixor.rixor.scrimmage.Scrimmage;
import com.projectrixor.rixor.scrimmage.countdowns.CountdownFactory;
import com.projectrixor.rixor.scrimmage.modules.Module;
import com.projectrixor.rixor.scrimmage.modules.ModuleInfo;
import com.projectrixor.rixor.scrimmage.modules.ModuleFactory;
import org.bukkit.configuration.InvalidConfigurationException;
import org.dom4j.Document;


public final class Map
{
    protected final Scrimmage scrimmage;
    protected final ModuleFactory factory;
    protected final MapLoader loader;
    protected final File dir;
    protected ModuleInfo context;
    protected boolean shouldReload = false;

    public Map(Scrimmage scrimmage, ModuleFactory factory, MapLoader loader, File dir) throws InvalidConfigurationException {
        this.scrimmage = scrimmage;
        this.factory = factory;
        this.loader = loader;
        this.dir = dir;
        try {
            this.context = load();
        } catch (Exception e) {
            throw new InvalidConfigurationException(e);
        }
    }

    private ModuleInfo load() throws JDOMException, IOException, InvalidConfigurationException {
        this.factory.getLogger().info("Trying to load modules for: " + this.dir);
        MapLoader loader1 = this.loader.getLoader(new File(this.dir, "map.xml"));
        Document doc = loader1.getDoc();

        Module context = new ModuleInfo(this.factory, new CountdownFactory(this.scrimmage), new RegionContext(), new FilterContext(), doc);

        if (!context.(InfoModule.class)) {
            throw new InvalidConfigurationException("must have info module");
        }
        return context;
    }

    public void markForReload() {
        this.shouldReload = true;
    }

    public boolean isMarkedForReload() {
        return this.shouldReload;
    }

    public void reload() {
        try {
            this.context = load();
            this.shouldReload = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ModuleContext getContext() {
        return this.context;
    }

    public MapInfo getInfo() {
        return ((InfoModule)this.context.getModule(InfoModule.class)).getMapInfo();
    }

    public String toString()
    {
        return getInfo().name;
    }

    public int compareTo(PGMMap other)
    {
        return getInfo().name.compareTo(other.getInfo().name);
    }
}
