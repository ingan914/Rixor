package com.projectrixor.rixor.scrimmage;

import org.bukkit.configuration.InvalidConfigurationException;

import java.io.File;
import java.io.IOException;


public final class Map
        implements Comparable<Map>
{
    protected final Scrimmage scrimmage;
    protected final ModuleFactoryContext factory;
    protected final SAXBuilder builder;
    protected final File dir;
    protected ModuleContext context;
    protected boolean shouldReload = false;

    public Map(Scrimmage scrimmage, ModuleFactoryContext factory, SAXBuilder builder, File dir) throws InvalidConfigurationException {
        this.scrimmage = scrimmage;
        this.factory = factory;
        this.builder = builder;
        this.dir = dir;
        try {
            this.context = load();
        } catch (Exception e) {
            throw new InvalidConfigurationException(e);
        }
    }

    private ModuleContext load() throws JDOMException, IOException, InvalidConfigurationException {
        this.factory.getLogger().info("Trying to load modules for: " + this.dir);
        Document doc = this.builder.build(new File(this.dir, "map.xml"));

        String protoStr = doc.getRootElement().getAttributeValue("proto");
        if (protoStr == null) {
            throw new InvalidConfigurationException("Map must specify a proto version.");
        }
        MapVersion mapProtoVer = MapVersion.parse(protoStr);
        if (!mapProtoVer.isCompatibleWith(PGM.MAP_PROTO_SUPPORTED)) {
            throw new InvalidConfigurationException("PGM supports " + PGM.MAP_PROTO_SUPPORTED + " however the map requires " + mapProtoVer);
        }

        ModuleContext context = new ModuleContext(this.factory, new CountdownContext(this.pgm), new RegionContext(), new FilterContext(), doc);

        if (!context.hasModule(InfoModule.class)) {
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
	
}
