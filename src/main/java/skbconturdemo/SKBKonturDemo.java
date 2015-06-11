package skbconturdemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SKBKonturDemo {
	
	public static void main(String[] args) {
		if (args.length != 1 && args[0] != null && !args[0].isEmpty()) {
			System.exit(1);
		}
		
		String pathToVocabulary = args[0];
		
		Vocabulary vocabulary = new TrieVocabulary(pathToVocabulary);
		UserQueries userQueries = new UserQueries(pathToVocabulary);

		ExecutorService executorService = Executors.newCachedThreadPool();
		
		for (String query : userQueries.getUserQueries()) {
			executorService.execute(new SuggestionQuery(vocabulary, query));
		}

		executorService.shutdown();
	}
	
}
