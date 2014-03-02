Overcast-Scrimmage
==================

Private Overcast Scrimmage plugin utilising the OCN XML system!

Todo List
---------
- [ ] XML Parsing of Filters
- [ ] Filters that actually have connected events
- [ ] Add Blitz support
- [ ] Add the Rage module
- [ ] Update to the new version of Overcast Tracker
- [ ] Add support for Gear maps

[PRE] Compiling your own version of ParaPGM
-------------------------------------------
First things first, you're going to need to download and **install** [Lombok] [6] onto Eclipse, this is so that the methods work. Make sure you restart Eclipse after install *cough* *cough*

Next, you're going to need to import the following jars into your Eclipse project!
- [SportBukkit Server] [1]
- [dom4j Library] [2]
- [SportBukkit API] [5]
- [Lombok] [6]

After that, export the plugin and you're ready to roll! Just add it to your plugins folder, and follow the setup process below.

1. Getting and Setting up SportBukkit
-------------------------------------
First of all, this plugin **requires** SportBukkit! So before going any further, if you don't have it, you'll need to compile or you can find a version of **SportBukkit** that my servers use, located at [CraftBukkit] [1]

If you don't know what **SportBukkit** is, then you're pretty silly and you need to place it as though it was your CraftBukkit/Spigot jar.

2. Getting the libraries you need
---------------------------------
ParaPGM requires a few libraries, these are pretty easy to setup.
Where your craftbukkit.jar is located, make a folder called "**libs**", and add [dom4j] [2] to it.

3. Creating and adding to the maps repository
---------------------------------------------
Before you can go any further, you must make sure that your Maps repository contains AT LEAST 1 map, without this the plugin will have a little cry and fail to load correctly.

You can see the format of the repository [here] [4] or [here] [3]. Those steps are pretty easy and no modifications should be needed to setup your server, as the map.xml is read just like PGM would!

The maps repository should be a folder named 'maps' in the root of the server folder. (This is the same location as the server jar.)

[1]: http://scrimmage1.teamloading.com/craftbukkit.jar "SportBukkit"
[2]: http://scrimmage1.teamloading.com/dom4j.jar "dom4j"
[3]: https://maps.oc.tc/ "Overcast Maps"
[4]: http://scrimmage1.teamloading.com/ "Scrimmage Maps"
[5]: http://scrimmage1.teamloading.com/bukkit.jar "SportBukkit API"
[6]: http://projectlombok.org/ "Project Lombok"
