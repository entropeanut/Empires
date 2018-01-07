package model;

import java.util.ArrayList;
import java.util.List;

public class TacticalMap {
	
	public enum Terrain {
		FOREST,
		GRASSLAND,
		SWAMP,
		WATER,
		DESERT,
		TUNDRA,
		MOUNTAIN,
		ROAD,
		DUNGEON,
		DUNGEON_WALL
	}
	
	int sizeX;
	int sizeY;
	List<Unit> units = new ArrayList<>();
	private Terrain[][] terrain;
	
	public TacticalMap(int sizeX, int sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		setTerrain(new Terrain[sizeX][sizeY]);
	}
	
	public TacticalMap addUnit(Unit unit) {
		units.add(unit);
		return this;
	}
	
	public int getSizeX() {
		return sizeX;
	}
	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}
	public int getSizeY() {
		return sizeY;
	}
	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}
	public List<Unit> getUnits() {
		return units;
	}
	public void setUnits(List<Unit> units) {
		this.units = units;
	}

	public Terrain[][] getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain[][] terrain) {
		this.terrain = terrain;
	}
}
