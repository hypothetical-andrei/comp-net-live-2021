package server;

public class State {
	static int counter = 0;
	
	public static void increase(int value) {
		State.counter += value;
	}
	
	public static void decrease(int value) {
		State.counter -= value;
	}
}
