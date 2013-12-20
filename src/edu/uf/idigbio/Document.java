package edu.uf.idigbio;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * this is an OCR document
 * 
 * @author miaochen
 *
 */
public class Document {
	
	
	static boolean do_stemming=false;
	static boolean remove_stopwords=true;
	static String stoplist="res/english.stop.txt";

	String text;
	// the hashmap for bag of words, word => frequency
	public HashMap<String, Integer> word_rawfreq;
	HashMap<String, Integer> word_boolean;
	HashMap<String, Double> word_normfreq;
	HashMap<String, Double> word_tfidf;

	public Document() {

	}

	public Document(String text) {
		// NOTE: here we replace spaces/tab/line break with a single " "
//		this.text = text.replaceAll("\\s+", " ");
	}

	public Document(File afile, String encoding) {
		
//		make the default encoding as utf-8
		if (encoding == null) {
			encoding="utf-8";
		}
		
		// NOTE: here we replace spaces/tab/line break with a single " "
		this.text = FileIO.getFileObjectAsString(afile, encoding);
		// System.out.println(text);
	}
	
	/***
	 * the weighting schema = raw tf
	 * 
	 * @param splitter
	 * @param tokenizer
	 */
//	public void toWordVectorRawFrequency (Tokenizer tokenizer) {
//				
//		word_rawfreq=new HashMap<String,Integer>();
//		if (remove_stopwords) {
//			ArrayList<String> stopwords=new ArrayList<String> ();
//			try{
//				BufferedReader br=new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(stoplist))));			
//				String strLine;
//				while((strLine=br.readLine())!=null){
//					stopwords.add(strLine.trim());
//				}				
//				br.close();
//			}catch (Exception e){
//				System.err.println("Error: "+e.getMessage());
//			}
//			
//			String[] sentences=splitter.split(text);
//			for (String sentence : sentences) {
//				String[] tokens=tokenizer.tokenize(sentence).split("\\s+");
//				
//				for (String token : tokens){
////					we first lowercase the token
//					token=token.toLowerCase();
////					examine if a. this token is a punctuation, b.skip stopwords, c.make sure this is word (no numerical or other things)
//					if(isPunctuation(token) || stopwords.contains(token) || !isWord(token) ){
//						continue;
//					}
//					if (do_stemming) {  //do stemming here
//						token=porter.stripAffixes(token);
//					}
//					
//					if (word_rawfreq.containsKey(token)){
//						int freq=word_rawfreq.get(token).intValue();
//						word_rawfreq.put(token, new Integer(freq+1));
//					}else{
//						word_rawfreq.put(token, new Integer(1));
//					}
//				}
//			}
//			
//		} else {
//			String[] sentences=splitter.split(text);
//			for (String sentence : sentences) {
//				String[] tokens=tokenizer.tokenize(sentence).split("\\s+");
//				
//				for (String token : tokens){
////					examine if this token is a punctuation
//					if(isPunctuation(token)){
//						continue;
//					}
////					here we lowercase the token
//					token=token.toLowerCase();
//					if (do_stemming) {  //do stemming here
//						token=porter.stripAffixes(token);
//					}				
//					
//					if (word_rawfreq.containsKey(token)){
//						int freq=word_rawfreq.get(token).intValue();
//						word_rawfreq.put(token, new Integer(freq+1));
//					}else{
//						word_rawfreq.put(token, new Integer(1));
//					}
//				}
//			}
//		}		
//
//	}
	
//	boolean isPunctuation (String token){
//		return token.equals(".")||token.equals("!")||token.equals("?")||token.equals(",");
//	}
	
	/***
	 * examines if a string is a real word or not, only contains alphabets
	 * 
	 * @param token
	 * @return
	 */
//	boolean isWord(String token) {
//		return token.matches("^[a-z]+$");
//	}
	
	/***
	 * 
	 * @param tokenizer
	 * @param n  --this is ngram number
	 * @return
	 */
	ArrayList<String> getNgramInArrayList (CharacterNgramExtractor ngramExtractor, int n) {
		
		return ngramExtractor.extractCharNgrams(text, n);
		
	}
	
	

	public static void main(String[] args) {
		
		

	}
	

}
