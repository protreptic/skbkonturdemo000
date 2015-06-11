package skbconturdemo;

import java.util.List;

public class SuggestionQuery implements Runnable {

	private Vocabulary vocabulary;
	private String prefix;
	
	public SuggestionQuery(Vocabulary vocabulary, String prefix) {
		this.vocabulary = vocabulary;
		this.prefix = prefix;
	}
	
	public void run() {
		List<VocabularyWord> suggestions = vocabulary.findSuggestions(prefix);
		
		if (!suggestions.isEmpty()) {
			synchronized (System.out) {
				for (VocabularyWord suggestion : suggestions) {
					System.out.println(suggestion.getText());
				}
				
				System.out.println();						
			}
		}
	}

}
