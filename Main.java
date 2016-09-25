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
		ArrayList <String> wordLadBFS  = getWordLadderBFS(start,end);
		printLadder(wordLadBFS);
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
		// TO DO
		ArrayList<String> inputs = new ArrayList<String>();
		for (int k = 0; k < 2; k++){
			String word = keyboard.next();
			if (word.equals("/quit")){
				System.out.println("quitting");
				return inputs; //return empty array List
			}
			inputs.add(word.toUpperCase());
		}	
		System.out.println(inputs.toString());
		return inputs;
	}
	
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
    	ArrayList <String> result = new ArrayList<String>();  // store words in word ladder
    	
    	if (start.equals(end)) // return empty array list of start == end
    		return result;
    	
    	result.add(start);	
		Set<String> dict = makeDictionary();
		Queue<String> queue = new LinkedList<String>();
		queue.add(start);										//Remove because it has been "discovered"
		int wordsInLayer = 1;
		boolean endFound = false;
		
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
						tempDict.remove(dictWord);			// "mark" word as visited already
						wordsInLayer++;
						result.add(dictWord);
						if (dictWord.equals(end)) {
							result.add(end);
							endFound = true;
						}
					}
				}
				dict = tempDict;
			}
		}
		
		if (endFound) {
			System.out.println("End Found");
			return result;
		}
		
		System.out.println("End Not Found");
		return new ArrayList<String>();  // return empty ArrayList
		
		//return null; // replace this line later with real return
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
		for (String s: ladder){
			System.out.println(s);
		}
	}
	// TODO
	// Other private static methods here
	
	/**
	  * This method determines if  one letter in currWord is different from nextWord
	  * @param currWord is the word at the current "node"
	  * @param nextWord is some other word to compare to. 
	  * @return true if one letter in currWord is different from nextWord
	  */
	private static boolean isRelated (String currWord, String nextWord){
		// check if words are the same length. Words should be 5 chars long for this lab
		if (currWord.length() != nextWord.length())
			return false;
		
		int diffs = 0;
		for (int i = 0; i < currWord.length(); i++){
			if (currWord.charAt(i) != nextWord.charAt(i)){
				diffs++;
			}
		}
		
		if (diffs == 1)
			return true;
		return false;  // should not encounter same word again
	}
	
}
