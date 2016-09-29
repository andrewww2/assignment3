/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Andrew Wong>
 * <aw27772>
 * <16450>
 * <Jonathan Walsh>
 * <jlw4699>
 * <16450>
 * Slip days used: <0>
 * Git URL: https://github.com/chernoalphha/assignment3/
 * Fall 2016
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static Set<String> dictSet;
	static ArrayList<String> dict;
	static boolean[] dfsMarked;
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb = new Scanner(System.in);	// input Scanner for commands
		ArrayList <String>inputs = parse(kb);
		kb.close();
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		
		//Get inputs (start of ladder, end of ladder)
		String start = inputs.get(0);
		String end = inputs.get(1);
		ArrayList <String> BFS =  getWordLadderBFS(start,end);
		System.out.println("Does BFS have duplicates? " + hasDuplicates(BFS));
		printLadder(BFS);
		
		ArrayList <String> DFS =  getWordLadderDFS(start,end);
		System.out.println("Does DFS have duplicates? " + hasDuplicates(DFS));
		printLadder(DFS);
		//printLadder(getWordLadderBFS(start,end));
		//printLadder(getWordLadderDFS(start,end));
	}
	
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		//We used these for our DFS algorithm
		//It made more sense for some of our data structures for DFS
		//    to be initialized globally
		dictSet = makeDictionary();
		dict = new ArrayList<String>(dictSet);
		dfsMarked = new boolean[dict.size()];
	}
	
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		
		ArrayList<String> inputs = new ArrayList<String>();
		for (int k = 0; k < 2; k++){
			String word = keyboard.next();
			if (word.equals("/quit")){
				System.out.println("quitting");
				return inputs; //return empty array List
			}
			inputs.add(word.toUpperCase());
		}	
		
		return inputs;
	}
	
	
	/**
	 * Creates a word ladder using a depth-first-search algorithm
	 * @param start : beginning of word ladder
	 * @param end : end of word ladder
	 * @return : an ArrayList of Strings that is our 'ladder' organized start -> end
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end)
	{
		ArrayList<String> ladder= new ArrayList<String>();	//The ladder we return
		boolean found = false;								//Lets us know if there is a path from start -> end
		
		try{
			found = myDfs(start, end, ladder);
			ArrayList<String> reversedLadder = new ArrayList<String>();	//We reverse the ladder because it is built end -> start
			for (int i = ladder.size(); i > 0; i--) {
				reversedLadder.add(ladder.get(i-1));
			}
			ladder = reversedLadder;
		}
		catch (StackOverflowError uber){		//If we get a stack overflow, we try searching from end -> start
			try {
				found = myDfs(end, start, ladder);
				//Not necessary to reverse ladder because we built it in the correct order
			}
			catch (StackOverflowError giveUp) {
				//Do nothing, after two overflows we just assume that there is no word ladder
			}
		}
		
		if (!found) {		
			start = start.toLowerCase();
			end = end.toLowerCase();
			System.out.println("no word ladder can be found between " + start + " and " + end + ".");
		}
		
		return ladder;
	}
	
	
	/**
	 * Recursively finds a ladder between two words
	 * @param start : beginning of the word ladder
	 * @param end : end of the word ladder
	 * @param ladder : ArrayList of word between start and end, built in this method
	 * @return true if a ladder is found; false if no ladder is found
	 * @throws StackOverflowError
	 */
	private static boolean myDfs(String start, String end, ArrayList<String> ladder) throws StackOverflowError {
		dfsMarked[dict.indexOf(start)] = true;
		if (start.equals(end)) {
			ladder.add(end);
			return true;
		}
		else {
			for (int i = 0; i < end.length(); i++) {
				if (start.charAt(i) != end.charAt(i)) {
					String newWord = start.substring(0, i) + end.charAt(i) + start.substring(i + 1);
					if (dict.contains(newWord)) {
						if (!dfsMarked[dict.indexOf(newWord)]) {
							if (myDfs(newWord, end, ladder)) {
								ladder.add(start);
								return true;
							}
						}
					}
				}
			} 
			for (String dictWord : dict) {
				if (isRelated(start, dictWord)) {
					if (!dfsMarked[dict.indexOf(dictWord)]) {
						if (myDfs(dictWord, end, ladder)) {
							ladder.add(start);
							return true;
						}
					}
				}
			}
		}
	return false;
	}
	
	
	/**
	 * Creates a word ladder using a breadth-first-search algorithm
	 * @param start : beginning of word ladder
	 * @param end : end of word ladder
	 * @return : void, but does print word ladder
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
    	ArrayList<String> wordLadder = new ArrayList<String>();
		Set<String> dict = makeDictionary();						//Complete list of words
		ArrayList<String> discoveredWords = new ArrayList<String>();	//Used to store discovered words 
		ArrayList<String> parentWords = new ArrayList<String>();	//Used to store 'parents' of discovered words 
		Queue<String> queue = new LinkedList<String>();
		queue.add(start);										//Remove because it has been "discovered"
		int wordsInLayer = 1;
		boolean endFound = false;
		
		if (start == end) {
			
		}
		
		while ((wordsInLayer > 0) && (!endFound)) {				//Ends when layer is empty or the word is found
			wordsInLayer = 0;
			int layerSize = queue.size();					//# of words in current layer
			for (int i = 0; i < layerSize; i++) {
				String currentWord = queue.remove();
				Set<String> tempDict = new HashSet<String>();
				tempDict.addAll(dict);
				for (String dictWord : dict) {
					if (isRelated(currentWord, dictWord)) {
						queue.add(dictWord);
						discoveredWords.add(dictWord);
						parentWords.add(currentWord);	//discoveredWord and parentWord will have same index
						tempDict.remove(dictWord);
						wordsInLayer++;
						if (dictWord.equals(end)) {
							endFound = true;
						}
					}
				}
				dict = tempDict;
			}
		}
		
		if (endFound) {
			wordLadder = findLadderBFS(start, end, discoveredWords, parentWords);
			//printLadder(wordLadder);
		}
		else {
			start = start.toLowerCase();			//formatting
			end = end.toLowerCase();
			System.out.println("no word ladder can be found between " + start + " and " + end + ".");
		}
		
		return wordLadder; // replace this line later with real return
	}
    
    
    /**
     * This method creates a set of String using a text file
     * @return a HashSet of Strings, a 'dictionary'
     */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	
	/**
	 * This method prints the ladder given the format provided in the assignment
	 * @param ladder : an ArrayList of Strings, each word just one letter apart
	 */
	public static void printLadder(ArrayList<String> ladder) {
		if (ladder.size() > 0){
			int length = ladder.size();
			String start = ladder.get(0);
			String end = ladder.get(length - 1);
			start = start.toLowerCase();		//formatting
			end = end.toLowerCase(); 
			int rungs = length - 2;				//length - start - end
			System.out.println("a " + rungs + "-rung word ladder exist between " + start + " and " + end + ".");
			for (String word : ladder) {
				word = word.toLowerCase(); 		//formatting
				System.out.println(word);
			}
		}
	}
	
	
	/**
	  * This method gets the user's input after he/she presses enter. 
	  * Also prints out a prompt for the user
	  * @param Scanner object connected to keyboard
	  * @return String that the user entered
	  */
	private static boolean isRelated (String currWord, String nextWord){
		// check if words are the same length. Words should be 5 chars long for this lab
		if (currWord.length() != nextWord.length())
			return false;
		
		int diffs = 0;
		for (int i = 0; i < currWord.length(); i++){
			if (currWord.charAt(i) != nextWord.charAt(i)){
				diffs++;
				if (diffs > 1) {
					return false;
				}
			}
		}
		
		if (diffs == 1)
			return true;
		return false;  // should not encounter same word again
	}

	
	/**
	 * This method used all of the discovered words to find the word ladder from start -> end
	 * @param start : beginning of ladder
	 * @param end : end of ladder
	 * @param words : ArrayList of all discovered words
	 * @param parents : ArrayList of the parents of the discovered words, with the same indices
	 * @return : an ArrayList of words from start -> end
	 */
	private static ArrayList<String> findLadderBFS(String start, String end, 
			ArrayList<String> words, ArrayList<String> parents) {
		ArrayList<String> wordLadder = new ArrayList<String>();
		String currentWord = end;
		wordLadder.add(currentWord);
		
		while (currentWord != start) {
			int i = words.indexOf(currentWord);		//Find location of current word
			currentWord = parents.get(i);			//Use that location to get parent
			wordLadder.add(currentWord);
		}
		
		//Now reverse the word ladder so you go from start -> end
		ArrayList<String> reversedWordLadder = new ArrayList<String>();
		for (int j = wordLadder.size(); j > 0; j--) {
			reversedWordLadder.add(wordLadder.get(j-1));
		}
		
		return reversedWordLadder;
	}
	
	private static boolean hasDuplicates(ArrayList<String> wLadder){
		if (wLadder.size() > 0){
			for (int i = 0; i < wLadder.size(); i++){
	    		for (int j = 0; j < wLadder.size(); j++){
	    			if (i != j && wLadder.get(i).equals(wLadder.get(j))){
	    				return true;
	    			}
	    		}
	    	}
			return false;
		}
    	
    	return false;
    }
	
}
