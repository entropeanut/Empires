package view.command;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Future;

import controller.event.BuildingEvent;
import model.Building;
import model.BuildingType;
import model.Empire;
import server.Controller;

public class BuildCommand extends Command {

    private final BuildingType buildingType;

    public BuildCommand(BuildingType building) {
        this.buildingType = building;
    }
    
    @Override
    public void execute(Controller controller, Empire empire) {
        Building building = new Building(buildingType);
        
		empire.getCapital().getBuildings().add(building);
		controller.fire(new BuildingEvent(BuildingEvent.Type.BEGIN, building));

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.MILLISECOND, buildingType.getBuildTime() * Controller.MS_IRL_PER_INGAME_HOUR);
		building.setCompletionDt(calendar.getTime());
		
		BuildingEvent completionEvent = new BuildingEvent(BuildingEvent.Type.COMPLETE, building);
		Future<?> futureCompletion = controller.schedule(completionEvent, buildingType.getBuildTime());
		building.setCompletionFuture(futureCompletion);
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }
}
