package view.terminal;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import controller.BuildTree;
import model.BuildingType;
import view.command.BuildCommand;
import view.command.Command;

public class CommandParser {
    
    private TerminalView view;
    private final List<Parser> parsers = Collections.unmodifiableList(Arrays.asList(new Parser() {
        
        private Pattern regex = Pattern.compile("build (.+)?", Pattern.CASE_INSENSITIVE);
        
        @Override
        public Command parse(String input) {
            Matcher matcher = regex.matcher(input);
            if (matcher.matches()) {
                String buildingName = matcher.group(1);
                BuildingType type = BuildTree.getInstance().get(buildingName);
                if (type != null) {
                    return new BuildCommand(type);
                } else if (buildingName != null) {
                    view.print("Unknown building type: " + buildingName);
                } else {
                    view.print("What kind of building?");
                }
            }
            return null;
        }
    }));
    
    interface Parser {
        Command parse(String input);
    }
    
    public CommandParser(TerminalView view) {
        this.view = view;
    }

    public List<Parser> getParsers() {
        return parsers;
    }
}
