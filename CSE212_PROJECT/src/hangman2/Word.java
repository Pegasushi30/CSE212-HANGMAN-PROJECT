package hangman2;

public class Word {
	String clue;
	String notInWord;
	String word;
	int timeOut;
	
	public Word(String clue, String notInWord, String word, int timeOut) {
		this.clue = clue;
		this.notInWord = notInWord;
		this.word = word;
		this.timeOut = timeOut;
	}
	
	// we should make getters.
	public String getWord() {
		if (word == null) {
			return "";
		}
		return word;
	}
	public String getClue() {
		if (clue == null) {
			return "";
		}
		return clue;
	}
	public String getNotInWord() {
		if (notInWord == null) {
			return "";
		}
		return notInWord;
	}
	public int getTimeOut() {
		if (word == null) {
			return 0;
		}
		return timeOut;
	}
}
