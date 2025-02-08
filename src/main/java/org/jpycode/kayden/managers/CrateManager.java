package org.jpycode.kayden.managers;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jpycode.kayden.Kayden;
import org.jpycode.kayden.models.Crate;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class CrateManager {

    private final Plugin plugin;
    private final File cratesFile;
    private FileConfiguration cratesConfig;
    private final HashMap<String, Crate> crates;

    public CrateManager(Plugin plugin) {
        this.plugin = plugin;
        this.crates = new HashMap<>();
        this.cratesFile = new File(plugin.getDataFolder(), "crates.yml");
        this.loadCrates();
    }

    public void createCrate(String name) {
        crates.put(name, new Crate(name));
        saveCrates();
    }

    public boolean crateExists(String name) {
        return crates.containsKey(name);
    }

    private void loadCrates() {
        if(!cratesFile.exists()) {
            try {
                cratesFile.createNewFile();
            } catch (IOException e) {
                System.out.println("failed to created crates.yml");
                e.printStackTrace();
            }
        }

        this.cratesConfig = YamlConfiguration.loadConfiguration(cratesFile);
        this.crates.clear();
        var cratesSection = cratesConfig.getConfigurationSection("crates");
        if(cratesSection == null) return;

        for (String crateName : cratesSection.getKeys(false)) {
            Crate crate = new Crate(crateName);

            //load locations
            var locationsSection = cratesSection.getConfigurationSection(crateName + ".locations");
            if (locationsSection != null) {
                for (String key : locationsSection.getKeys(false)) {
                    String worldName = locationsSection.getString(key + ".world");
                    World world = plugin.getServer().getWorld(worldName);
                    if(world != null) {
                        double x = locationsSection.getDouble(key + ".x");
                        double y = locationsSection.getDouble(key + ".y");
                        double z = locationsSection.getDouble(key + ".z");
                        crate.addLocation(new Location(world, x, y, z));

                    }
                }
            }

            //load rewards
            crates.put(crateName, crate);
        }

    }

    public void saveCrates() {
        cratesConfig.set("crates", null);
        var cratesSection = cratesConfig.createSection("crates");

        for (var crate : crates.values()) {
            var crateSection = cratesSection.createSection(crate.getName());


            //save locations
            ConfigurationSection locationsSection = crateSection.createSection("locations");
            int i = 0;
            for (Location loc : crate.getLocations()) {
                ConfigurationSection locationSection = locationsSection.createSection(String.valueOf(i++));
                locationSection.set("world", loc.getWorld().getName());
                locationSection.set("x", loc.getX());
                locationSection.set("y", loc.getY());
                locationSection.set("z", loc.getZ());
            }

            //save rewards
        }

        try {
            cratesConfig.save(cratesFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
