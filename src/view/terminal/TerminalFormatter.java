package view.terminal;

import static view.Table.Alignment.CENTER;
import static view.Table.Alignment.LEFT;
import static view.Table.Alignment.RIGHT;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import model.TacticalMap;
import terminal.Terminal;
import view.Table;
import view.Table.Alignment;

public abstract class TerminalFormatter implements ContentHandler {

	private static final int PREFERRED_BORDER_WIDTH = 2;
	   
	public enum TextType {
		GENERAL,
		STEP,
		BR,
		COMMAND,
		BUILDING,
		RESOURCE
	}
	
	private final Terminal terminal;
	private final XMLReader xmlReader;

	public static TerminalFormatter newInstance(Terminal terminal) {
		switch (terminal.getProtocol()) {
		case TELNET:
			return new TelnetFormatter(terminal);
		default:
			throw new IllegalArgumentException("Unknown protocol: " + terminal.getProtocol());
		}
	}
	
	protected TerminalFormatter(Terminal terminal) {
		this.terminal = terminal;
		SAXParserFactory spf = SAXParserFactory.newInstance();
	    spf.setNamespaceAware(true);
	    
		try {
			SAXParser saxParser = spf.newSAXParser();
			xmlReader = saxParser.getXMLReader();
		} catch (ParserConfigurationException | SAXException e) {
			throw new RuntimeException("Error configuring parser", e);
		}
		xmlReader.setContentHandler(this);
	}
	
	public void print(String text) {
		getTerminal().printLine(text);
		getTerminal().printLine("");
	}

	public void printLine(String text) {
		getTerminal().printLine(text);
	}
	
	public void printNoLineBreak(String text) {
		getTerminal().print(text);
	}

	public void format(String xml) {
		try {
			xmlReader.parse(new InputSource(new StringReader(xml)));
			getTerminal().printLine("");
			getTerminal().printLine("");
		} catch (IOException | SAXException e) {
			throw new RuntimeException("Error parsing text: " + xml, e);
		}
	}

    public abstract void print(Table table);

	public abstract void print(TacticalMap map);

    protected void repeat(char ch, int amount, StringBuilder builder) {
        for (int i = 0; i < amount; i++) {
            builder.append(ch);
        }
    }

    protected List<String> toText(Table table) {

        Object[] headers = table.getHeaders();
        Alignment[] alignments = table.getColAlignments();
        int[] widths = new int[headers.length];
        for (int i = 0; i < widths.length; i++) {
            widths[i] = headers[i].toString().length();
            for (Object[] row : table.getRows()) {
                widths[i] = Math.max(row[i].toString().length(), widths[i]);
            }
        }
        
        List<String> lines = new ArrayList<>(table.getRows().size());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < headers.length; i++) {
            int padding = widths[i] - headers[i].toString().length();
            repeat(' ', padding/2, builder);
            builder.append(headers[i]);
            repeat(' ', padding/2 + padding%2, builder);
            repeat(' ', PREFERRED_BORDER_WIDTH, builder);
        }
        lines.add(builder.toString());
        builder = new StringBuilder();
        
        for (int i = 0; i < headers.length; i++) {
            repeat('-', widths[i], builder);
            repeat(' ', PREFERRED_BORDER_WIDTH, builder);
        }
        
        for (Object[] cells : table.getRows()) {
            lines.add(builder.toString());
            builder = new StringBuilder();
            for (int i = 0; i < cells.length; i++) {
                int length = cells[i].toString().length();
                Alignment alignment = alignments[i];
                if (cells[i] instanceof Number) {
                    alignment = RIGHT;
                }
                if (alignment == CENTER) {
                    int padding = widths[i] - length;
                    repeat(' ', padding/2, builder);
                    builder.append(cells[i]);
                    repeat(' ', padding/2 + padding%2, builder);
                    repeat(' ', PREFERRED_BORDER_WIDTH, builder);
                } else {
                    if (alignment == RIGHT) {
                        repeat(' ', widths[i] - length, builder);
                    }
                    builder.append(cells[i]);
                    if (alignment == LEFT) {
                        repeat(' ', widths[i] - length, builder);
                    }
                    repeat(' ', PREFERRED_BORDER_WIDTH, builder);
                }
            }
        }
        lines.add(builder.toString());
        return lines;
    }
	public Terminal getTerminal() {
		return terminal;
	}
}
