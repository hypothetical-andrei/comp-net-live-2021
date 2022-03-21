package server;

public class State {
	static int counter = 0;
	
	public static void increase(int amount) {
		State.counter += amount;
	}
	
	public static void decrease(int amount) {
		State.counter -= amount;
	}
}
