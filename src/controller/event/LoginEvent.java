package controller.event;

import model.Empire;

public class LoginEvent extends Event {
	
	private final Empire empire;

	public LoginEvent(Empire empire) {
		this.empire = empire;
	}

	public Empire getEmpire() {
		return empire;
	}
}
