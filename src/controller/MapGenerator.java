package controller;

import static java.lang.Math.min;
import static java.lang.Math.random;

import model.TacticalMap;
import model.TacticalMap.Terrain;

public class MapGenerator {

	private TacticalMap map;

	public MapGenerator(int sizeX, int sizeY) {
		map = new TacticalMap(sizeX, sizeY);
		
	}
	
	public static TacticalMap dungeon(int sizeX, int sizeY) {
		MapGenerator generator = new MapGenerator(sizeX, sizeY);

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				generator.map.getTerrain()[x][y] = Terrain.DUNGEON;
			}
		}
		return generator.map;
	}
	
	public static TacticalMap forestPaths(int sizeX, int sizeY) {
		MapGenerator generator = new MapGenerator(sizeX, sizeY);

		for (int x = 0; x < sizeX; x++) {
			for (int y = 0; y < sizeY; y++) {
				generator.map.getTerrain()[x][y] = Terrain.GRASSLAND;
			}
		}
		
		for (int i = 0; i < (sizeX*sizeY/36); i++) {
			int rx = (int) (random()*sizeX);
			int ry = (int) (random()*sizeY);
			generator.growForest(generator.map,rx,ry,1);
		}
		
		for (int i = 0; i < (sizeX*sizeY/18000); i++) {
//			int rx
//			createRiver(map, 
		}
		return generator.map;
	}
	
	private void growForest(TacticalMap map, int x, int y, double fertilizer) {
		if (map.getTerrain()[x][y] == Terrain.FOREST) {
			return;
		}

		int neighbors = 0;
		if (x+1 >= map.getSizeX() || map.getTerrain()[x+1][y] == Terrain.FOREST) {
			neighbors++;
		}
		if (x-1 < 0 || map.getTerrain()[x-1][y] == Terrain.FOREST) {
			neighbors++;
		}
		if (y+1 >= map.getSizeY() || map.getTerrain()[x][y+1] == Terrain.FOREST) {
			neighbors++;
		}
		if (y-1 < 0 || map.getTerrain()[x][y-1] == Terrain.FOREST) {
			neighbors++;
		}
		
		if (random() < fertilizer + (.16*Math.pow(neighbors, 2))) {
			map.getTerrain()[x][y] = Terrain.FOREST;
			if (fertilizer > 0) {
				fertilizer -= min(.2, fertilizer);
			}
		} else {
			if (neighbors == 4) {
				throw new RuntimeException(fertilizer+ "");
			}
			return;
		}
		
		if (x+1 < map.getSizeX()) { 
			growForest(map, x+1, y, fertilizer);
		}
		if (x-1 >= 0) { 
			growForest(map, x-1, y, fertilizer);
		}
		if (y+1 < map.getSizeY()) { 
			growForest(map, x, y+1, fertilizer);
		}
		if (y-1 >= 0) { 
			growForest(map, x, y-1, fertilizer);
		}
	}

}
