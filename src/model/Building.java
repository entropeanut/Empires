package model;

import java.util.Date;
import java.util.concurrent.Future;

public class Building {

    private final BuildingType type;
    private Date completionDt;
    private Future<?> completionFuture;

    public Building(BuildingType type) {
        this.type = type;
    }
    public BuildingType getType() {
        return type;
    }
	public Date getCompletionDt() {
		return completionDt;
	}
	public void setCompletionDt(Date completionDt) {
		this.completionDt = completionDt;
	}
	public Future<?> getCompletionFuture() {
		return completionFuture;
	}
	public void setCompletionFuture(Future<?> completionFuture) {
		this.completionFuture = completionFuture;
	}
}
