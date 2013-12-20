package edu.uf.idigbio;

import java.io.File;
import java.util.ArrayList;

/***
 * this is to extract character level n-gram from a given string
 * 
 * @author miaochen
 *
 */


public class CharacterNgramExtractor {

	
	public CharacterNgramExtractor () {
		
	}
	
	
	/***
	 * IT'S A TESTING FUNCTION!!! NEED TO FINISH
	 * 
	 *  it takes a text input, get its n-gram
	 * it considers <start> and <end>, and making them as a token
	 * 
	 * for example, string "this is a test" would be something like this internallly
	 * "<start> this is a test <end>"
	 * and the bigrams would be [<start>,this], ..., [test <end>]
	 * 
	 * @param text
	 * @return
	 */
	public ArrayList<String> extractCharNgramsTest (String text, int n) {
		
//		hardcoded here, for expressing start and end of the text
//		this is not the best solution. hopefully it doesn't interfere with the char n-gram such as <start> in further steps
//		i.e. I hope there is no char n-gram possiblity which is written as <start> or <end>!
		
		String[] lines=text.split("\n");
		
		if (lines==null)
			return null;
		
		ArrayList<String> ngrams=new ArrayList<String> ();
		
		for (String line : lines) {
			
//			trim end of line here, if there's end-of-line
			char[] chars=line.trim().toCharArray();
			
			if (chars==null)
				continue;
			
			/***
			 * i indicates the position of the starting point of the current n-gram (character level)
			 * 
			 */
			for (int i=0; i<chars.length-n+1; i++) {
				
				StringBuilder sbCurrentNgram=new StringBuilder();
				
				for (int j=0; j<n; j++) {				
					sbCurrentNgram.append(Character.toString(chars[i+j]));
				}		
				ngrams.add(sbCurrentNgram.toString());
			}
			
//			add the n-gram chars for <start> and <end>
			if ((chars.length + 1) >= n) {
				String startngram="<start>";
				String endngram="<end>";
				
				for (int j=0; j<n-1; j++) {
					startngram+=chars[j];
					endngram+=chars[chars.length-n+1+j];
				}
				
				ngrams.add(startngram);
				ngrams.add(endngram);
			}
			
		}
		
		return ngrams;
		

		
	}
	
	/***
	 * it takes a text input, get its n-gram
	 * it takes the string literally, without considering its <start> and <end> tags
	 * 
	 * @param text
	 * @return
	 */
	public ArrayList<String> extractCharNgrams (String text, int n) {
		
		String[] lines=text.split("\n");
		
		if (lines==null)
			return null;
		
		ArrayList<String> ngrams=new ArrayList<String> ();
		
		for (String line : lines) {
			
//			trim end of line here, if there's end-of-line
			char[] chars=line.trim().toCharArray();
			
			if (chars==null)
				continue;
			
			/***
			 * i indicates the position of the starting point of the current n-gram (character level)
			 * 
			 */
			for (int i=0; i<chars.length-n+1; i++) {
				
				StringBuilder sbCurrentNgram=new StringBuilder();
				
				for (int j=0; j<n; j++) {				
					sbCurrentNgram.append(Character.toString(chars[i+j]));
				}		
				ngrams.add(sbCurrentNgram.toString());
			}
			
		}
		
		return ngrams;
		
	}
	
	
	public static void main (String[] args) {
		
//		String text="HERBARIUM OF IOWA STATE UNIVERSITY Studies in Lasiecis";
//		Tokenizer tokenizer=new Tokenizer();
//		ArrayList<String> ngramsChars=tokenizer.tokenizeToNgramChars(text, 2);
//
//		for (String onengram : ngramsChars) {
//			
//			System.out.println(onengram);
//			
//		}
		
		String filedir="/Users/miaochen/Documents/Projects/iDigBio/silver_ocr/1659813.txt";
		Document doc=new Document(new File(filedir), "utf-8");
		System.out.println(doc.text);
		
		CharacterNgramExtractor tokenizer=new CharacterNgramExtractor();
		
		ArrayList<String> ngramsChars=tokenizer.extractCharNgrams(doc.text, 2);
		for (String onengram : ngramsChars) {
			
			System.out.println(onengram);
			
		}
		
	}
	

}
