package model;

import java.util.HashSet;
import java.util.Set;

public class Empire {

    private String name;
    
    private final Set<Region> regions = new HashSet<>();

    public Empire() {
		getRegions().add(new Region());
    }
    public Region getCapital() {
        return getRegions().iterator().next();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
	public Set<Region> getRegions() {
		return regions;
	}

}
