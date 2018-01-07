package view.command;

import controller.event.LoginEvent;
import model.Empire;
import server.Controller;

public class LoginCommand extends Command {

	@Override
	public void execute(Controller controller, Empire empire) {
        empire = new Empire();
        controller.setEmpire(empire);
        controller.fire(new LoginEvent(empire));
	}
}
