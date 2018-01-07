package controller.event;

public class TutorialEvent extends Event {

	private final String message;

	public TutorialEvent(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
