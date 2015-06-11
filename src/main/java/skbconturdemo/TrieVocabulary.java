package skbconturdemo;

import java.io.BufferedReader; 
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class TrieVocabulary implements Vocabulary {
	
	private Map<String, Integer> occr = new HashMap<String, Integer>();
	
	private class TrieNode {
		
		public int value;
		public TrieNode firstChild;
		public TrieNode nextSibling;

		public TrieNode(int value) {
			this.value = value;
		}
		
	}
	
	private TrieNode rootNode = new TrieNode('r');
	private int size = 0;
	private int maxDepth;
	
	public TrieVocabulary(String path) {
		openDictionary(path); 
	}

	private void openDictionary(String path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));

 			int n = Integer.valueOf(reader.readLine());
			
			for (int i = 0; i < n; i++) {
				String temp = reader.readLine();
				
				StringTokenizer tokenizer = new StringTokenizer(temp);
				
				String text = tokenizer.nextToken();
				Integer occurrenceFrequency = Integer.valueOf(tokenizer.nextToken()); 
				
				occr.put(text, occurrenceFrequency);
				
				addWord(text);
			}
			
			reader.close();
			reader = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<VocabularyWord> findSuggestions(String prefix) {
		List<VocabularyWord> words = new ArrayList<VocabularyWord>();
		
		String[] suggestions = suggest(rootNode, prefix, 0);
		
		if (suggestions != null) {
			for (String string : suggestions) {
				VocabularyWord word = new VocabularyWord();
				word.setText(string); 
				word.setOccurrenceFrequency(occr.get(string));  
	  			
				words.add(word);
			}
			
			if (words.size() > 1) {
				Collections.sort(words, new Comparator<VocabularyWord>() {
	
					public int compare(VocabularyWord o1, VocabularyWord o2) {
						if (o1.getOccurrenceFrequency() == o2.getOccurrenceFrequency()) return 0;
						if (o1.getOccurrenceFrequency() > o2.getOccurrenceFrequency()) return -1;
						
						return 1;
					}
						
				});		
			}
		}
		
		return words;
	}
	
	private boolean addWord(String word) {
		if (add(rootNode, word, 0)) {
			size++;
			int n = word.length();
			if (n > maxDepth) maxDepth = n;
			
			return true;
		}
		
		return false;
	}

	private boolean add(TrieNode root, String word, int offset) {
		if (offset == word.length()) return false;
		int c = word.charAt(offset);

		TrieNode last = null, next = root.firstChild;
		while (next != null) {
			if (next.value < c) {
				last = next;
				next = next.nextSibling;
			} else if (next.value == c) {
				return add(next, word, offset + 1);
			}
			
			else break;
		}

		TrieNode node = new TrieNode(c);
		
		if (last == null) {
			root.firstChild = node;
			node.nextSibling = next;
		} else {
			last.nextSibling = node;
			node.nextSibling = next;
		}

		for (int i = offset + 1; i < word.length(); i++) {
			node.firstChild = new TrieNode(word.charAt(i));
			node = node.firstChild;
		}
		
		return true;
	}

	private void collect(TrieNode root, List<String> words, char[] chars, int pointer) {
		TrieNode node = root.firstChild;
		
		while (node != null) {
			if (node.firstChild == null) {
				words.add(new String(chars, 0, pointer));
			} else {
				chars[pointer] = (char) node.value;
				collect(node, words, chars, pointer + 1);
			}
			node = node.nextSibling;
		}
	}

	private String[] suggest(TrieNode root, String word, int offset) {
		if (offset == word.length()) {
			List<String> words = new ArrayList<String>(size);
			char[] chars = new char[maxDepth];
			for (int i = 0; i < offset; i++)
				chars[i] = word.charAt(i);
			collect(root, words, chars, offset);
			if (words.size() > 10) {
				return words.subList(0, 10).toArray(new String[10]);
			}
			
			return words.toArray(new String[words.size()]);
		}
		
		int c = word.charAt(offset);

		TrieNode next = root.firstChild;
		while (next != null) {
			if (next.value < c) next = next.nextSibling;
			else if (next.value == c) return suggest(next, word, offset + 1);
			else break;
		}
		
		return null;
	}

}
