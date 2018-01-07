package view.terminal;

import static java.util.Collections.unmodifiableSet;
import static terminal.telnet.TelnetTerminal.CR;
import static terminal.telnet.TelnetTerminal.LF;
import static view.terminal.TerminalFormatter.TextType.BUILDING;
import static view.terminal.TerminalFormatter.TextType.COMMAND;
import static view.terminal.TerminalFormatter.TextType.RESOURCE;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import model.TacticalMap;
import model.TacticalMap.Terrain;
import model.Unit;
import terminal.Terminal;
import terminal.telnet.BasicColor;
import terminal.telnet.Color;
import terminal.telnet.TelnetTerminal;
import view.Table;

public class TelnetFormatter extends TerminalFormatter {

    private final Set<TextType> CAPITALIZED_TYPES = unmodifiableSet(EnumSet.of(COMMAND, BUILDING, RESOURCE));
     
    private final TelnetTerminal terminal;
    private final Deque<TextType> stack = new ArrayDeque<>();
    
	public TelnetFormatter(Terminal terminal) {
		super(terminal);
		this.terminal = (TelnetTerminal) terminal;
	}

    public void print(Table table) {
    	printLines(toText(table));
    }

	private void printLines(List<String> text) {
		text.forEach(line ->  printLine(line));
		printLine("");
	}

	@Override
	public void print(TacticalMap map) {

	    final Map<TacticalMap.Terrain,Color[]> terrainColors = new HashMap<>();
    	terrainColors.put(Terrain.FOREST, new Color[]{new Color(0, 2, 0), new Color(0, 1, 0)});
    	terrainColors.put(Terrain.GRASSLAND, new Color[]{new Color(0, 4, 0), new Color(0, 5, 0)});
    	terrainColors.put(Terrain.SWAMP, new Color[]{new Color(1, 1, 0), new Color(1, 2, 0)});
    	terrainColors.put(Terrain.WATER, new Color[]{new Color(0, 0, 4), new Color(0, 0, 5)});
    	terrainColors.put(Terrain.DESERT, new Color[]{new Color(4, 4, 0), new Color(5, 5, 0)});
    	terrainColors.put(Terrain.TUNDRA, new Color[]{new Color(4, 4, 5), new Color(5, 5, 5)});
    	terrainColors.put(Terrain.MOUNTAIN, new Color[]{new Color(2, 2, 2), new Color(1, 1, 1)});
    	terrainColors.put(Terrain.ROAD, new Color[]{new Color(130), new Color(137)});
    	terrainColors.put(Terrain.DUNGEON, new Color[]{Color.greyscale(3), Color.greyscale(6)});
    	terrainColors.put(Terrain.DUNGEON_WALL, new Color[]{new Color(0), new Color(0)});
	    
		Color grey = Color.greyscale(2);
		StringBuilder builder = new StringBuilder();
		for (int y = 0; y < map.getSizeY(); ++y) {
			for (int x = 0; x < map.getSizeX(); ++x) {
				builder.append(BasicColor.DEFAULT);
				String tile = "  ";
				for (Unit unit : map.getUnits()) {
					if (unit.getX() == x && unit.getY() == y) {
						tile = String.format("%02d", map.getUnits().indexOf(unit)+1);
					}
				}
//				if (2*x + y > 17) {
//					builder.append(terrainColors.get(Terrain.FOREST)[(x+y)%2].bg);
//				} else if (2*x + y > 7) {
//					builder.append(terrainColors.get(Terrain.WATER)[(x+y)%2].bg);
//				} else {
//					builder.append(terrainColors.get(Terrain.GRASSLAND)[(x+y)%2].bg);
//				}
				Color color = terrainColors.get(map.getTerrain()[x][y])[(x+y)%2];
				builder.append(color.bg);
				if (color.luminance() > .5) {
					builder.append(BasicColor.BLACK).append(tile);
				} else {
					builder.append(BasicColor.BRIGHT).append(BasicColor.WHITE).append(tile);
				}
			}
			builder.append(BasicColor.DEFAULT).append(CR).append(LF);
		}
        printLine(builder.toString());
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		stack.push(TextType.GENERAL);
	}

	@Override
	public void startDocument() throws SAXException {
	}

	@Override
	public void endDocument() throws SAXException {
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
		TextType textType = TextType.valueOf(localName.toUpperCase());
		stack.addFirst(textType);
		format(textType);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		stack.pop();
		terminal.print(BasicColor.DEFAULT.toString());
		format(stack.peek());
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String text = String.valueOf(ch, start, length);
		if (CAPITALIZED_TYPES.contains(stack.peek())) {
			terminal.print(text.toUpperCase());
		} else {
			terminal.print(text);
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) throws SAXException {
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
	}

	@Override
	public void processingInstruction(String target, String data) throws SAXException {
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
	}

	private void format(TextType textType) {
		switch(textType) {
		case GENERAL:
			terminal.print(BasicColor.DEFAULT.toString());
			break;
			
		case STEP:
			terminal.print(BasicColor.BRIGHT.toString());
			terminal.print(BasicColor.BLUE.toString());
			break;
			
		case BR:
			terminal.printLine("");
			break;
		
		case COMMAND:
			terminal.print(BasicColor.WHITE.toString());
			terminal.print(BasicColor.BRIGHT.toString());
			break;
			
		case BUILDING:
			terminal.print(BasicColor.WHITE.toString());
			terminal.print(BasicColor.BRIGHT.toString());
			break;
			
		case RESOURCE:
			terminal.print(BasicColor.WHITE.toString());
			terminal.print(BasicColor.BRIGHT.toString());
			break;
			
		default:
			break;
		}
	}

}
