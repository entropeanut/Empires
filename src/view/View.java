package view;

import view.command.Command;

public abstract class View {

	public static interface Executor {
		void execute(Command command);
	}
}
