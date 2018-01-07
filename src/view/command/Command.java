package view.command;

import model.Empire;
import server.Controller;

public abstract class Command {

    public abstract void execute(Controller controller, Empire empire);
    
}
