package controller.event;

import model.Building;

public class BuildingEvent extends Event {

	public enum Type {
		BEGIN,
		COMPLETE
	}
	
    private final Building building;
	private final Type type;

    public BuildingEvent(Type type, Building building) {
		this.type = type;
		this.building = building;
    }
    
    public Building getBuilding() {
        return building;
    }

	public Type getType() {
		return type;
	}
}
