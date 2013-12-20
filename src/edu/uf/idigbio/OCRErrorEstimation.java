package edu.uf.idigbio;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/****
 * 
 * given a word token, calculate its probability 
 * 
 * @author miaochen
 *
 */

public class OCRErrorEstimation {
	
	private HashMap<String,Double> ngram_prob;
	
	public OCRErrorEstimation () {
		
		
		
	}
	
	/***
	 * by supplying a pre-computed probability of ngrams from some (arbitrary) corpus,
	 * we put these info in a hashmap
	 * 
	 * @param ngramProbList
	 */
	public void setProbabilityHashMap (String ngramProbListDir) {
		
		ngram_prob=FileIO.readFileToHash_StringDouble(ngramProbListDir);
		
	}
	

	public void setProbabilityHashMap (HashMap<String,Double> hm) {
		
		ngram_prob=hm;
		
	}
	
	public int getLengthOfProbabilityHashMap () {
		
		return ngram_prob.size();
		
	}
	
	
	public double estimateProbability (String wordstring, int ngram) {
		
		WordString wstring=new WordString(wordstring);
		ArrayList<String> ngramsList=wstring.getNgramInArrayList(ngram);
		
		double sumLogProb=0;  //this is log of the Multipled Probabilities
		
		for (String aNgramString : ngramsList) {
			
			double prob=ngram_prob.get(aNgramString).doubleValue();
			double logProb=Math.log(prob);
			
			sumLogProb+=logProb;
		}
		
		return sumLogProb;
	}
	
	/***
	 * it examines if the file name follows idigbio file naming convention, to make sure
	 * it's a good file
	 * 
	 * @param filename
	 * @return
	 */
	static boolean isGoodFileName(String filename) {
		return filename.matches("^[0-9]+\\S+.txt");
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
		
		String corpusDir="/Users/miaochen/Documents/Projects/iDigBio/silver_ocr";
//		String ngramProbListDir="/Users/miaochen/Documents/Projects/iDigBio/trigram-prob.txt";
		String outDir="/Users/miaochen/Documents/Projects/iDigBio/ocr_confidence";
		String encoding="utf-8";
		int ngram=3;
		CharacterNgramExtractor tokenizer=new CharacterNgramExtractor();
		
//		for getting probability of n-grams
		CharacterNgramCounterForCorpus ngramCounterForCorpus=new CharacterNgramCounterForCorpus();
		ngramCounterForCorpus.countFrequencyInCorpus(corpusDir, tokenizer, ngram, encoding);
		HashMap<String,Integer> ngram_freq=ngramCounterForCorpus.ngram_freq;
		
		ProbabilityGeneratorFromNgram probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> ngram_prob=probGenerator.getProbability(ngram_freq);				
		
		OCRErrorEstimation ocrestimator=new OCRErrorEstimation();
//		ocrestimator.setProbabilityHashMap(ngramProbListDir);
		ocrestimator.setProbabilityHashMap(ngram_prob);
//		System.out.println(ocrestimator.getLengthOfProbabilityHashMap());
		
//		Tokenizer tokenizer=new Tokenizer();
		
		String[] filenames=new File(corpusDir).list();
		
		for (String filename : filenames) {
			
			if (!isGoodFileName(filename)) {
				System.out.println("Bad filename "+filename+", skipped");
				continue;
			}else{
				System.out.println("Processing file "+filename);
			}
			
			Document doc=new Document(new File(corpusDir+File.separator+filename), encoding);
//		    String[] tokens=tokenizer.tokenizeToWords(doc.text);
//		    
//		    if (tokens!=null) {
//		    	
//		    	StringBuilder content=new StringBuilder();
//		    	
//		    	HashMap<String,Double> token_logProb=new HashMap<String,Double>();
//		    	
//		    	for (String token : tokens) {
//		    		
//		    		double logProb=ocrestimator.estimateProbability(token, ngram);
////		    		System.out.println(token+"\t"+logProb);
//		    		token_logProb.put(token, new Double(logProb));
//		    		
//		    	}		    	
//		    	
//		    	Map sorted=sortByValues(token_logProb);
//		    	for (Iterator iter=sorted.keySet().iterator(); iter.hasNext(); ) {
//					Object k=(Object) iter.next();
//					content.append(k.toString()+"\t"+token_logProb.get(k).toString()+"\n");
//				}
//		    	
//		    	String outFileDir=outDir+File.separator+filename;
//		    	FileIO.writeFile(outFileDir, content.toString());
//		    }
		    
			
		}
		
		
		
	}

}
