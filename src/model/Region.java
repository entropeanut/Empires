package model;

import java.util.HashSet;
import java.util.Set;

public class Region {

    private String name;
    
    private final Set<Building> buildings = new HashSet<>();

    public Region() {
    }
    
    public String getName() {
        return name;
    }

    public Set<Building> getBuildings() {
        return buildings;
    }
}
