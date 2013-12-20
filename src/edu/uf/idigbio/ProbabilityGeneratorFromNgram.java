package edu.uf.idigbio;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/***
 * given n-gram frequency generated from a corpus,
 * it generates the probability of the n-grams
 * 
 * @author miaochen
 *
 */

public class ProbabilityGeneratorFromNgram {
	
	public ProbabilityGeneratorFromNgram () {
		
		
		
	}
	
	HashMap<String,Double> getProbability (HashMap<String,Integer> ngram_freq) {
		
		HashMap<String,Double> ngram_prob=new HashMap<String,Double> ();
		int total=getTotalNgramCount(ngram_freq);
		
		System.out.println("The total number of trigrams is "+total);
		
		for (Iterator iter=ngram_freq.keySet().iterator(); iter.hasNext(); ) {
			
			String ngramstring=(String) iter.next();
			int freq=ngram_freq.get(ngramstring).intValue();
			
			double prob=(double)freq/(double)total;
			ngram_prob.put(ngramstring, prob);
		}
		
		return ngram_prob;
	}
	
	int getTotalNgramCount (HashMap<String,Integer> hm) {
		
		int total=0;
		
		for (Iterator iter=hm.keySet().iterator(); iter.hasNext(); ) {
			
			String keystring=(String) iter.next();
			int freq=hm.get(keystring).intValue();
			total+=freq;
			
		}
		
		return total;
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
		
		int ngram=3;
		CharacterNgramExtractor tokenizer=new CharacterNgramExtractor();
		String encoding="utf-8";
		
		String corpusDir="/Users/miaochen/Documents/Projects/iDigBio/silver_ocr";
		CharacterNgramCounterForCorpus ngramCounterForCorpus=new CharacterNgramCounterForCorpus();
		ngramCounterForCorpus.countFrequencyInCorpus(corpusDir, tokenizer, ngram, encoding);
		HashMap<String,Integer> ngram_freq=ngramCounterForCorpus.ngram_freq;			
		
		ProbabilityGeneratorFromNgram probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> ngram_prob=probGenerator.getProbability(ngram_freq);
		
		String outdir="/Users/miaochen/Documents/Projects/iDigBio/trigram-prob.txt";
		StringBuilder out=new StringBuilder();
		Map sorted = sortByValues (ngram_prob);
		for (Iterator iter=sorted.keySet().iterator(); iter.hasNext(); ) {
			Object k=(Object) iter.next();
			out.append(k.toString()+"\t"+ngram_prob.get(k).toString()+"\n");
		}
		FileIO.writeFile(outdir, out.toString());
		
	}

}
