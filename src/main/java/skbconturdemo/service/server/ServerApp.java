package skbconturdemo.service.server;

import skbconturdemo.TrieVocabulary;

public class ServerApp {

	public static void main(String[] args) {
		if (args.length != 2) {
			usageDescription();
			System.exit(1);
		}
		
		String pathToVocabulary = args[0];
		int port = Integer.valueOf(args[1]);
		
		new ServerNetworkThread(new TrieVocabulary(pathToVocabulary), port).start();
	}

	public static void usageDescription() {
		System.out.println("Usage: <path_to_vocabulary> <port>");
	}

}
