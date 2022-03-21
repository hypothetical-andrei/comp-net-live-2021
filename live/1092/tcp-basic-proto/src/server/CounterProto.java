package server;

public class CounterProto {

	public String process(String input) {
		String[] items = input.split("\\s");
		if (items.length < 2) {
			return "Command not valid";
		} else {
			switch (items[0]) {
			case "inc":
				State.increase(Integer.parseInt(items[1]));
				return String.format("State is now : %d", State.counter);
			case "dec":
				State.decrease(Integer.parseInt(items[1]));
				return String.format("State is now : %d", State.counter);
			default:
				return "Command not recognized";
			}
		}
	}
	
}
