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
 * Git URL: https://github.com/chernoalphha/422CAssignment3
 * Fall 2016
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	
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
		
		
		// TODO methods to read in words, output ladder
		String start = inputs.get(0);
		String end = inputs.get(1);
		getWordLadderBFS(start,end);
		
		printLadder(getWordLadderDFS(start,end));
	}
	
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
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
	
	public static ArrayList<String> getWordLadderDFS(String start, String end)
	{
		ArrayList<String> dfsResult= new ArrayList<String>();
		Set<String> dict = makeDictionary();
		ArrayList<String> dict2 = new ArrayList<String>(dict);
		
		//myDfs(start, end, dfsResult, dict);
		boolean[] marked = new boolean[dict2.size()];
		for (boolean i : marked) {
			i = false;
		}
		boolean found = myDFS2(start, end, dfsResult, marked);
		if (!found) {
			System.out.println("Help!");
		}
		//Reverse ladder to get correct ladder
		ArrayList<String> reversedLadder = new ArrayList<String>();
		for (int i = dfsResult.size(); i > 0; i--) {
			reversedLadder.add(dfsResult.get(i-1));
		}
		return reversedLadder;
	}
	
	public static boolean myDfs(String start, String end, ArrayList <String> parentN, Set <String> dict) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		//Set<String> dict = makeDictionary();
		Set<String> tempDict = new HashSet<String>();
		tempDict.addAll(dict);
		
		if (start.equals(end)){
			parentN.add(end);
			//TODO: ADD ALL ANCESTORS OF THE DESTINATION
			return true;
		}
		else if (dict.size() == 0){
			return false;
		}
		else{
			for (String dictWord : dict) {
				if (isRelated(start, dictWord)) {
					//visit neighbor of words\
					// mark word as "visited"
					tempDict.remove(dictWord);
					if (myDfs(dictWord, end, parentN, tempDict)){
						parentN.add(start);
						break;
					}
					
					
					//tempDict.remove(dictWord); 
					//dict = tempDict;
				}
			}
			//TODO: // return false when there is no children to be expanded
			
		}
		
		// TODO more code
		
		return false; // replace this line later with real return
	}
	
	private static boolean myDFS2(String start, String end, ArrayList<String> ladder, boolean[] marked) {
		Set<String> dict2 = makeDictionary();
		ArrayList<String> dict = new ArrayList<String>(dict2);
		if (start.equals(end)) {
			ladder.add(end);
			return true;
		}
		else if (dict.size() == 0) {
			return false;
		}
		else {
			/*for (int i = 0; i < end.length(); i++) {
				if (start.charAt(i) != end.charAt(i)) {
					String newWord = start.substring(0, i) + end.charAt(i) + start.substring(i + 1);
					if (dict.contains(newWord)) {
						if (!marked[dict.indexOf(newWord)]) {
							marked[dict.indexOf(newWord)] = true;
							if (myDFS2(newWord, end, ladder, marked)) {
								ladder.add(start);
								return true;
							}
						}
					}
				}
			} */
			for (String dictWord : dict) {
				if (isRelated(start, dictWord)) {
					if (!marked[dict.indexOf(dictWord)]) {
						marked[dict.indexOf(dictWord)] = true;
						if (myDFS2(dictWord, end, ladder, marked)) {
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
	 * creates a word ladder using a breadth-first-search algorithm
	 * @param start : beginning of word ladder
	 * @param end : end of word ladder
	 * @return : void, but does print word ladder
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
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
			ArrayList<String> wordLadder = findLadderBFS(start, end, discoveredWords, parentWords);
			printLadder(wordLadder);
		}
		else {
			start = start.toLowerCase();			//formatting
			end = end.toLowerCase();
			System.out.println("no word ladder can be found between " + start + " and " + end + ".");
		}
		
		return null; // replace this line later with real return
	}
    
    
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
	
	
	public static void printLadder(ArrayList<String> ladder) {
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
	
}
