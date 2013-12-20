package edu.uf.idigbio;

import java.io.File;
import java.util.ArrayList;

public class WordLevelTokenizer {
	
	public WordLevelTokenizer () {
		
	}
	
	/***
	 * word level tokenizer is a simple one, just delimit by whitespace
	 * 
	 * due to the nature of OCR text (messy)
	 * 
	 * @param text
	 * @return
	 */
	public ArrayList<String> tokenizeToWords(String text) {		

		String[] lines=text.split("\n");
		
		if (lines==null)
			return null;
		
		ArrayList<String> words=new ArrayList<String> ();
		
		for (String line : lines) {
			
//			trim end of line here, if there's end-of-line
			String[] tokens=line.trim().split("\\s+");
			
			if (tokens==null) {
				continue;
			}
			
			for (String token : tokens) {
				
				words.add(token);
				
			}
			
		}
		
		return words;		
	}
	
	/***
	 * it keeps the line information, and return array of array like this
	 * [[a,b,c],
	 * [b,c,d],
	 * [b,c,d]]
	 * 
	 * @return
	 */
	public ArrayList<ArrayList<String>> tokenizerToWordsByLine (String text) {
		

		String[] lines=text.split("\n");
		
		if (lines==null)
			return null;
		
		ArrayList<ArrayList<String>> wordsByLine=new ArrayList<ArrayList<String>> ();
		
		for (String line : lines) {
			
			ArrayList<String> wordsInOneLine=new ArrayList<String> ();
			
//			trim end of line here, if there's end-of-line
			String[] tokens=line.trim().split("\\s+");
			
			if (tokens==null) {
				continue;
			}
			
			for (String token : tokens) {
				
				if (token.equalsIgnoreCase("")){
					continue;
				}
				wordsInOneLine.add(token);				
			}
			wordsByLine.add(wordsInOneLine);
		}
		
		return wordsByLine;
	}
	
	
	
	public static void main (String[] args) {
		
		String filedir="/Users/miaochen/Documents/Projects/iDigBio/silver_ocr/1659813.txt";
		Document doc=new Document(new File(filedir), "utf-8");
		System.out.println(doc.text);
		
		WordLevelTokenizer wordtokenizer=new WordLevelTokenizer();
		ArrayList<String> words=wordtokenizer.tokenizeToWords(doc.text);
		
		for (String word : words)
			System.out.println(word);
		
	}
	
	

}
