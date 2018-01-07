package server;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.Tutorial;
import controller.event.Event;
import model.Empire;
import view.View;
import view.command.Command;
import view.terminal.TerminalView;
import terminal.Terminal;

public class Controller implements View.Executor, Terminal.Listener {

	/** 1 in-game millennium = 1 year IRL */
	public static final int MS_IRL_PER_INGAME_HOUR = 60*60;

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

	private final TerminalView view;
    private Empire empire;
    
    private Set<Observer> observers = new HashSet<>(1);

    public Controller(Terminal terminal) {
        
        view = new TerminalView(terminal);
        terminal.addListener(this);
        
        view.addExecutor(this);
        this.addObserver(view);

        view.login();
    }

    @Override
    public void onCharacter(Terminal terminal, char data) {
        // Wait for EOL
    }

	@Override
	public void onInput(Terminal terminal, int numChars, String current) {
		// Wait for EOL
	}

    @Override
    public void onLine(Terminal terminal, String line) {
    	executor.execute(() -> {
    		try {
    			view.handleCommand(line);
    		} catch (Throwable t) {
    			log.error("Error processing command: " + line, t);
    			throw t;
    		}
    	});
    }

	public Future<?> schedule(Event event, int days) {

		return executor.schedule(() -> {
			try {
				fire(event);
			} catch (Throwable t) {
				log.error("Error processing scheduled event: " + event, t);
				throw t;
			}
		}, days * Controller.MS_IRL_PER_INGAME_HOUR, TimeUnit.MILLISECONDS);
	}

	public void fire(Event event) {
		for (Observer observer : observers) {
			observer.observe(event);
		}
	}

	@Override
	public void execute(Command command) {
		command.execute(this, empire);
	}
    
	public static interface Observer {
    	void observe(Event event);
    }

	public void addObserver(Observer observer) {
		observers.add(observer);
	}

	public void setEmpire(Empire empire) {
		if (this.empire != null) {
			throw new IllegalStateException("Empire cannot be reassigned.");
		}
		this.empire = empire;

        Tutorial tutorial = new Tutorial(this);
        addObserver(tutorial);
        view.addExecutor(tutorial);
	}

}
