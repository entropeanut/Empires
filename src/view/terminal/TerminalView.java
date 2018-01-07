package view.terminal;
import static view.Table.Alignment.CENTER;
import static view.Table.Alignment.RIGHT;

import java.util.HashSet;
import java.util.Set;

import controller.MapGenerator;
import controller.event.BuildingEvent;
import controller.event.Event;
import controller.event.LoginEvent;
import controller.event.TutorialEvent;
import model.Building;
import model.Empire;
import model.Region;
import model.TacticalMap;
import model.Unit;
import server.Controller;
import terminal.Terminal;
import view.Table;
import view.View;
import view.command.Command;
import view.command.LoginCommand;
import view.terminal.CommandParser.Parser;

public class TerminalView extends View implements Controller.Observer {
    
    private final TerminalFormatter formatter;
    
    private final CommandParser commandParser;
    private final Set<Executor> executors = new HashSet<>(1);
	private Empire empire;


    public TerminalView(Terminal terminal) {
        terminal.setPrompt(">");
        formatter = TerminalFormatter.newInstance(terminal);
        
        this.commandParser = new CommandParser(this);
    }

    public void addExecutor(Executor executor) {
		executors.add(executor);
    }

    public void login() {
//        terminal.print("Empire Name:");
//        String name = terminal.readLine();
//        terminal.printLine("Now in " + empire.getCapital().getName() + ".");
    	for (Executor executor : executors) {
    		executor.execute(new LoginCommand());
    	}
    }

    public void handleCommand(String input) {

        formatter.printLine("");
        
//        print(new Table("Unit", "Health", "Attack", "Damage", "Defense", "Action")
//            .addRow("1. White Wizard", 24, 30, 36, 12, "Fireball")
//            .addRow("2. Knight", 50, 45, 40, 50, "Charge")
//            .addRow("3. Orc", 40, 40, 35, 40, "?")
//            .addRow("4. Elven Archer", 36, 70, 50, 36, "Ranged Attack")
//            .addRow("5. Ogre", 80, 35, 60, 80, "?"));
//        terminal.readLine();
//        String input = terminal.readLine();
        
        if ("status".equalsIgnoreCase(input)) {
        	Table table = new Table("Construction", "% Complete");
        	for (Region region : empire.getRegions()) {
        		for (Building building : region.getBuildings()) {
        			if (System.currentTimeMillis() < building.getCompletionDt().getTime()) {
        				long remaining = building.getCompletionDt().getTime() - System.currentTimeMillis();
						int buildTime = building.getType().getBuildTime() * Controller.MS_IRL_PER_INGAME_HOUR;
						table.addRow(building.getType().getName(), 100 - Math.floorDiv(remaining*100, buildTime));
        			}
        		}
        	}
			print(table);
        } else if ("regions".equalsIgnoreCase(input)) {
            print(new Table("Neighboring Region", "Population", "Area", "Type")
              .addRow("Quansic", 1000, 150, "Tundra")
              .addRow("Melecan", 200, 300, "Forest")
              .addRow("Tohkyl", 400, 50, "Grassland")
              .addRow("Tyaga", 600, 523, "Forest"));
        } else if ("Affinity".equalsIgnoreCase(input)) {
        	print(new Table("Affinities", "Forest", "Grassland", "Marshland", "Desert", "Tundra", "Mountain", "Subterranean")
    			.addRow("Human", "High", "High", "Low", "Low", "Very Low", "Medium", "Medium")
    			.addRow("Elf", "Very High", "Low", "Medium", "Low", "Very Low", "Medium", "Low", "Low")
    			.addRow("Orc", "Medium", "Medium", "High", "Low", "Very Low", "Low", "High"));
        } else if ("production".equalsIgnoreCase(input)) {
            print(new Table("Building", "Workers", "Production", "Amount per Day", "Backlog (Days)")
                .setColAlignment(1, CENTER).setColAlignment(4, RIGHT)
                .addRow("Mine", "10/8", "Cart of Ore", 17, "?") 
                .addRow("Blacksmith", " 4/3*", "Bronze Chestplate, Tier 2", 2, 17)
                .addRow("Logging Camp", " 8/8", "Log", 40, 3600)
                .addRow("Lumbermill", "  3/10", "Wooden Rod", 50, 10)
                .addRow("Arcanery", "1/3", "Wooden Staff, Tier 1", 5, 50)
                .addRow("Stonemason", "2/2", "Brick", 500, 20)
                .addRow("Convoy", "12/12",  "Shipment", 0.2, 3));
            formatter.printNoLineBreak("Footnotes: * 1 apprentice");
//          print(new Table("Building", "Amount", "Workers", "Production", "Rate")
//          .addRow("Farm", "x4", "20", "Crops", 4));
        	print(new Table("Job", "# Buildings", "# Workers", "Avg. Skill", "Production", "Completes")
				.addRow("Farm", 10, 20, 1.0, "Bushel of Crops", " 2 days")
				.addRow("Construction", "N/A", 4, 1.0, "Lumbermill", "12 days"));
        } else if ("score".equalsIgnoreCase(input)) {
            print(new Table("Overall", 458)
                .addRow("Population", 2480)
                .addRow("Army", 1405)
                .addRow("Economy", 12403)
                .addRow("Technology", "Era IV"));
        } else if ("resources".equalsIgnoreCase(input)) {
            print(new Table("Resource", "Stored", "Production", "Consumption", "Exhausted")
                .addRow("Iron", 12, .3, 0, "40 days")
                .addRow("Logs", 1200, 50, 1, "N/A")
                .addRow("Lumber", 30, 1, 5, "6 days"));
        } else if ("workers".equalsIgnoreCase(input)) {
//            print(new Table("Area", "Workers")
//                .addRow("Farming", 30)
//                .addRow("Construction (Blacksmith)", 4)),
//                .addRow("Research (Greater Magical Vortex)");
        } else if ("army".equalsIgnoreCase(input)) {
            print(new Table("Map ID", "Unit", "Count", "Level (Avg)", "Health (%)", "Action", "Target", "Map ID")
                .addRow("01", "Udan (Champion Stalwart)", 1, 1, 96, "Attacking", "Orcish Grunt", 4)
                .addRow("02", "Dwarven Footman", 40, 3, 35, "Moving", "Orcish Grunt", 4)
                .addRow("03", "Elven Archer", 18, 2, 67, "Attacking", "Orcish Warlock", 2));
        } else if ("craft".equalsIgnoreCase(input)) {
            print(new Table("Item", "Wood", "Bronze", "Steel")
                .addRow("Shortsword", 1, "", 3)
                .addRow("Chainmail", "", 2, 6)
                .addRow("Full Platemail", "", 4, 24));
        } else if ("map".equalsIgnoreCase(input)) {
//        	print(new TacticalMap(10, 10)
//        			.addUnit(new TacticalMap.Unit(2, 4))
//        			.addUnit(new TacticalMap.Unit(3, 6)));
//        	print(MapGenerator.forestPaths(terminal.getWidth()*2/4, terminal.getHeight()-3)
        	print(MapGenerator.dungeon(10, 10)
        			.addUnit(new Unit(4, 1))
        			.addUnit(new Unit(5, 1))
        			.addUnit(new Unit(6, 0))
        			.addUnit(new Unit(3, 0)));
        } else if ("inspect".equalsIgnoreCase(input)) {
        	print(new Table("Name", "Armor", "Weapon", "Misc1", "Misc2", "Mount", "Facing", "Position")
    			.addRow("Gwen", "Chainmail II", "Shortsword", "Pale Bauble", "", "Falabella", "North", "Front")
    			.addRow("Antoan", "Steel Plate", "Longsword", "Shaela's Penant", "Ring of Strength II", "Warhorse", "North", "Front"));
        } else if ("routes".equalsIgnoreCase(input)) {
            print(new Table("#", "Location", "Type", "Strategic Value", "Enemy Defenses", "Start", "Leads To")
                .addRow(1, "Shafron Valley", "Valley", 3, "40x Risen Warrior", "Yes", "2, 3, 4")
                .addRow("", "", "", "", " 2x Risen Witch", "", "")
                .addRow(2, "Meandron Uplands", "Hills", 4, "None", "Yes", "1, 3")
                .addRow(3, "Shafron Lake", "Lake", 2, "30x Risen Warior", "No", "1, 2, 4")
                .addRow("", "", "", "", " 2x Durmani Assasin", "", "")
                .addRow(4, "Shafron Creek", "River Crossing", 8, "60x Risen Warrior", "No", "1, 3")
                .addRow("", "", "", "", " 1x Risen Warlock", "", ""));
        } else {

            Command command = parseCommand(input);
            if (command != null) {
                for (Executor executor : executors) {
                	executor.execute(command);
                }
            } else {
                print("Unknown command: " + input);
            }
        }
        
        formatter.printNoLineBreak("> ");
    }

	private Command parseCommand(String input) {
        for (Parser parser : commandParser.getParsers()) {
            Command command = parser.parse(input);
            if (command != null) {
                return command;
            }
        }
        return null;
    }

	@Override
	public void observe(Event event) {
        if (event instanceof BuildingEvent) {
        	BuildingEvent buildEvent = (BuildingEvent) event;
    		String buildingName = buildEvent.getBuilding().getType().getName().toLowerCase();
        	switch(buildEvent.getType()) {
        	case BEGIN:
				print("Construction of a " + buildingName + " has begun.");
        		return;
        	case COMPLETE:
        		print("Construction of a " + buildingName + " is complete.");
        		break;
        	}
        } else if (event instanceof LoginEvent) {
			LoginEvent loginEvent = (LoginEvent) event;
			this.empire = loginEvent.getEmpire();
			
		} else if (event instanceof TutorialEvent) {
			TutorialEvent tutorialEvent = (TutorialEvent) event;
			formatter.format(tutorialEvent.getMessage());
		}

        formatter.printNoLineBreak("> ");
    }

    public void print(String line) {
        formatter.print(line);
    }

    public  void print(Table table) {
    	formatter.print(table);
    }

    private void print(TacticalMap map) {
    	formatter.print(map);
	}
}
