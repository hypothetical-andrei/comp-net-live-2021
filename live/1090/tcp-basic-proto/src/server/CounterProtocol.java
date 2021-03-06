package server;

public class CounterProtocol {

	public String process(String command) {
		String[] items = command.split("\\s");
		if (items.length < 2) {
			return "Command did not have enough params";
		} else {
			switch (items[0]) {
			case "inc":
				State.increase(Integer.parseInt(items[1]));
				return String.format("Counter is now %d\n", State.counter);
			case "dec":
				State.decrease(Integer.parseInt(items[1]));
				return String.format("Counter is now %d\n", State.counter);
			default:
				return "Command not recognized";
			}
		}
	}

}
