package controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ResourceBundle;

import controller.event.BuildingEvent;
import controller.event.Event;
import controller.event.TutorialEvent;
import server.Controller;
import view.View;
import view.XmlResourceBundle;
import view.command.Command;

public class Tutorial implements View.Executor, Controller.Observer {
	
	private final ResourceBundle tutorialResources =  ResourceBundle.getBundle("TutorialResources", new XmlResourceBundle.Control());
	
	public enum Step {
		BUILD_LUMBERMILL {
			@Override
			public boolean isSatisfied(Event event) {
				if (event instanceof BuildingEvent) {
					BuildingEvent buildingEvent = (BuildingEvent) event;
					return "Lumbermill".equals(buildingEvent.getBuilding().getType().getName());
				}
				return false;
			}
		},
		STATUS {
			public boolean isSatisfied(Event event) {
				if (event instanceof BuildingEvent) {
					BuildingEvent buildingEvent = (BuildingEvent) event;
					return "Lumbermill".equals(buildingEvent.getBuilding().getType().getName())
							&& BuildingEvent.Type.COMPLETE.equals(buildingEvent.getType());
				}
				return false;
			}
		},
		BUILD_LOGGING_CAMP {
			@Override
			public boolean isSatisfied(Event event) {
				if (event instanceof BuildingEvent) {
					BuildingEvent buildingEvent = (BuildingEvent) event;
					return "Logging Camp".equals(buildingEvent.getBuilding().getType().getName());
				}
				return false;
			}
		};

		public boolean isSatisfied(Event event) {
			return false;
		}
		public boolean isSatisfied(Command command) {
			return false;
		}
	}
	
	private Iterator<Step> iter = Arrays.asList(Step.values()).iterator();
	private Step currentStep;
	private Controller controller;
	
	public Tutorial(Controller controller) {
		this.controller = controller;
		nextStep();
	}

	@Override
	public void observe(Event event) {
		if (currentStep.isSatisfied(event)) {
			nextStep();
		}
	}

	@Override
	public void execute(Command command) {
		if (currentStep.isSatisfied(command)) {
			nextStep();
		}
	}
	
	private void nextStep() {
		if (iter.hasNext()) {
			currentStep = iter.next();
			String message = tutorialResources.getString(currentStep.name());
			TutorialEvent tutorialEvent = new TutorialEvent(message);
			controller.schedule(tutorialEvent, 1);
		}
	}
}
