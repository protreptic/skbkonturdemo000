package skbconturdemo;

import java.util.List;

public interface Vocabulary {
	
	public List<VocabularyWord> findSuggestions(String prefix);
	
}
