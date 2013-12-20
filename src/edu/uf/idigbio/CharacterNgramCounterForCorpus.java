package edu.uf.idigbio;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class CharacterNgramCounterForCorpus {
	
	public HashMap<String,Integer> ngram_freq=new HashMap<String,Integer> ();
	
	
	public CharacterNgramCounterForCorpus () {
		
		
		
	}
	
	/***
	 * 
	 * 
	 * @param corpusDir
	 * @param tokenizer
	 * @param ngram --number of grams, a.k.a. ngram
	 * @return
	 */
	public void countFrequencyInCorpus (String corpusDir, CharacterNgramExtractor tokenizer, int ngram, String encoding) {
				
		String[] filenames=new File(corpusDir).list();
		
		for (String filename : filenames) {
			
			if (!isGoodFileName(filename)) {
				System.out.println("Bad filename "+filename+", skipped");
				continue;
			}else{
				System.out.println("Processing file "+filename);
			}
			
			Document doc=new Document(new File(corpusDir+File.separator+filename), encoding);
			ArrayList<String> ngramlist=doc.getNgramInArrayList(tokenizer, ngram);
			
			for (String currentNgram : ngramlist) {
				
				if (ngram_freq.containsKey(currentNgram)) {
					int freq=ngram_freq.get(currentNgram);
					ngram_freq.put(currentNgram, new Integer(freq+1));
				}else{
					ngram_freq.put(currentNgram, 1);
				}
				
			}
			
		}
		
	}
	
	/***
	 * it examines if the file name follows idigbio file naming convention, to make sure
	 * it's a good file
	 * 
	 * @param filename
	 * @return
	 */
	boolean isGoodFileName(String filename) {
//		return filename.matches("^[0-9]+\\S+.txt");
		return !filename.startsWith(".");
	}
	
	/***
	 * sort a hashmap by value
	 * 
	 * @param map
	 * @return
	 */
	static 	public <K,V extends Comparable<V>> Map<K,V> sortByValues (final Map<K,V> map) {
		Comparator<K> valueComparator =  new Comparator<K>() {
	        public int compare(K k1, K k2) {
	            int compare = map.get(k2).compareTo(map.get(k1));
	            if (compare == 0) return 1;
	            else return compare;
	        }
	    };
	    Map<K, V> sortedByValues = new TreeMap<K, V>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
	
	
	public static void main (String[] args) {
		
		int ngram=2;
		CharacterNgramExtractor tokenizer=new CharacterNgramExtractor();
		String encoding="utf-8";
		
		String corpusDir="/Users/miaochen/Documents/Projects/iDigBio/data/label-data/herb/gold/ocr";
		CharacterNgramCounterForCorpus ngramCounterForCorpus=new CharacterNgramCounterForCorpus();
		
		ngramCounterForCorpus.countFrequencyInCorpus(corpusDir, tokenizer, ngram, encoding);
		HashMap<String,Integer> charngram_freq=	ngramCounterForCorpus.ngram_freq;
		
//		for (Iterator iter=ngram_freq.keySet().iterator(); iter.hasNext(); ) {
//			
//			String ngramstring=(String) iter.next();
//			int freq=ngram_freq.get(ngramstring);
//			
//			System.out.println(ngramstring+":"+freq);
//			
//		}
		
		String outdir="/Users/miaochen/Documents/Projects/iDigBio/herbgold-bigram-freq.txt";
		StringBuilder out=new StringBuilder();
		Map sorted = sortByValues (charngram_freq);
		for (Iterator iter=sorted.keySet().iterator(); iter.hasNext(); ) {
			Object k=(Object) iter.next();
			out.append(k.toString()+"\t"+charngram_freq.get(k).toString()+"\n");
		}
		FileIO.writeFile(outdir, out.toString());
		
	}
	

}
