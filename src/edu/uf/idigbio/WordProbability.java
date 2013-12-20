package edu.uf.idigbio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/***
 * 
 * given a word, make it as characters, and then compute its probability according to the trained
 * n-gram freq/probability
 * 
 * here is doing probability estimation based on bigram, should do tri-gram in the future
 * it's not doing smoothing now, should do simple add-one smoothing in the future
 * 
 * @author miaochen
 *
 */

public class WordProbability {
	
	public HashMap<String,Double> ngram_prob;
	public HashMap<String,Double> nless1gram_prob;
	
	public WordProbability (HashMap<String,Double> ngram_prob, HashMap<String,Double> nless1gram_prob) {
		
		this.ngram_prob=ngram_prob;
		this.nless1gram_prob=nless1gram_prob;
		
	}
	
	public double estimate (String wordstring, int ngram) {
		
		double est=0;
		
		WordString wstring=new WordString(wordstring);
		ArrayList<String> ngramsListFromWordstring=wstring.getNgramInArrayList(ngram);
//		System.out.println(ngramsListFromWordstring);
		ArrayList<String> nless1gramListFromWordString=wstring.getNgramInArrayList(ngram-1);
//		System.out.println(nless1gramListFromWordString);
		
		double sumNgramProb=addLogProbabilityOfNgramFromWordstring(ngramsListFromWordstring, ngram_prob);
		double sumNless1gramProb=addLogProbabilityOfNgramFromWordstring(nless1gramListFromWordString, nless1gram_prob);
		
		est=sumNgramProb-sumNless1gramProb;
		
//		System.out.println(sumNgramProb+"-"+sumNless1gramProb+"="+(sumNgramProb-sumNless1gramProb));
//		System.out.println("est is "+est);
		
		return est;
	}
	
	double addLogProbabilityOfNgramFromWordstring(ArrayList<String> ngramsListFromWordstring, HashMap<String,Double> trainNgram_prob) {
		
		double sumLogProb=0;  //this is log of the Multipled Probabilities
		
		for (String ngramString : ngramsListFromWordstring) {
			
			if (trainNgram_prob.containsKey(ngramString)) { //if the n-gram occurs in the training corpus
				
				double prob=trainNgram_prob.get(ngramString).doubleValue();
				double logProb=Math.log(prob);
				
				sumLogProb+=logProb;
				
			}else {//if not exist, then now just assign a 0
				
				
			}
			
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
//		return filename.matches("^[0-9]+\\S+.txt");
		return !filename.startsWith(".");
	}
		
	
	
	public static void main (String[] args) {
		
//		This is the code for computing based on bi-gram (or tri-gram)
		
		int ngram=3;
		
		String trainCorpusDir="/Users/miaochen/Documents/Projects/iDigBio/data/label-data/herb/gold/ocr";
		String ocrCorpusDir="/Users/miaochen/Documents/Projects/iDigBio/data/label-data/herb/silver/ocr";
		String outFolderDir="/Users/miaochen/Documents/Projects/iDigBio/herb_silver_trigram_confidence";
		String outHtmlFolder="/Users/miaochen/Documents/Projects/iDigBio/herb_silver_trigram_html";
		String encoding="utf-8";
		
		CharacterNgramExtractor charTokenizer=new CharacterNgramExtractor();
		WordLevelTokenizer wordTokenizer=new WordLevelTokenizer();
		
//		for getting probability of bi-grams
		CharacterNgramCounterForCorpus bigramCounterForCorpus=new CharacterNgramCounterForCorpus();
		bigramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram, encoding);
		ProbabilityGeneratorFromNgram probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> bigram_prob=probGenerator.getProbability(bigramCounterForCorpus.ngram_freq);	
		
//		for getting probability of uni-grams
		CharacterNgramCounterForCorpus unigramCounterForCorpus=new CharacterNgramCounterForCorpus();
		unigramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram-1, encoding);
		probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> unigram_prob=probGenerator.getProbability(unigramCounterForCorpus.ngram_freq);
		
		WordProbability wordprob=new WordProbability(bigram_prob, unigram_prob);		

		String[] filenames=new File(ocrCorpusDir).list();
		
		for (String filename : filenames) {
			
			if (!isGoodFileName(filename)) {
				System.out.println("Bad filename "+filename+", skipped");
				continue;
			}else{
				System.out.println("Processing file "+filename);
			}
			
			
		    
			
		}
		
		
		
	}
	
	

}
