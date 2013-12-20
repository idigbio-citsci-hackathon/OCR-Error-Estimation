package edu.uf.idigbio;

import java.util.ArrayList;

/***
 * this is about operations on a word string (a token)
 * 
 * @author miaochen
 *
 */

public class WordString {
	
	private String wordtext;
	
	public WordString(String wordtext) {
		
		this.wordtext=wordtext;
		
	}
	

	public ArrayList<String> getNgramInArrayList (int ngram) {
		
		ArrayList<String> ngramsList=new ArrayList<String> ();
		char[] chars=wordtext.trim().toCharArray();
		
		for (int i=0; i<chars.length-ngram+1; i++) {
			
			StringBuilder sbCurrentNgram=new StringBuilder();
			
			for (int j=0; j<ngram; j++) {				
				sbCurrentNgram.append(Character.toString(chars[i+j]));
			}
			
			ngramsList.add(sbCurrentNgram.toString());
			
		}
		
		return ngramsList;
		
	}
	
	
	public static void main (String[] args) {
		
		String s="BOTANICAL";
		WordString ws=new WordString(s);
		
		ArrayList<String> ngramsList=ws.getNgramInArrayList(3);
		
		for (String aNgramString : ngramsList) {
			
			System.out.println(aNgramString);
			
		}
		
	}

}
