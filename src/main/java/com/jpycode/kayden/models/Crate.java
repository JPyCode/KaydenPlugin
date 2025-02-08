package com.jpycode.kayden.models;

import lombok.Getter;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

@Getter
public class Crate {
    private final String name;
    private final Set<Location> locations;

    public Crate(String name) {
        this.name = name;
        this.locations = new HashSet<>();
    }


    public void addLocation(Location location) {
        locations.add(location);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
    }


}
