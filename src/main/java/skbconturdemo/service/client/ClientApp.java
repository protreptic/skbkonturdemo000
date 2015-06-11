package skbconturdemo.service.client;


public class ClientApp {
	
	public static void main(String[] args) {
		if (args.length != 2) {
			usageDescription();
			System.exit(1);
		}
		
		String server = args[0];
		int port = Integer.valueOf(args[1]);
		
		new ClientNetworkThread(server, port).start();
	}
	
	public static void usageDescription() {
		System.out.println("Usage: ServiceClient <server> <port>");
	}
	
}
