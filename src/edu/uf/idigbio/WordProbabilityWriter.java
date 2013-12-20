package edu.uf.idigbio;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/***
 * 
 * given a document, tokenize it to words, compute probability of each word in the document
 * write the output to different format
 * 
 * 0=word\t(probability)
 * 1=html style
 * 
 * @author miaochen
 *
 */

public class WordProbabilityWriter {
	
	public WordProbabilityWriter () {
		
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
	

	int roundDecimal (double d) {
		
		return (int) Math.round(d);
		
	}
	
	/***
	 * this is actually for part of the html code, collaborating with Jason Best
	 * to be applied on CSS for highlighting different OCR confidence scores
	 * 
	 * @param ngram
	 * @param trainCorpusDir
	 * @param ocrCorpusDir
	 * @param outHtmlFolder
	 * @param encoding
	 */
	public void writeHTMLOutput (int ngram, String trainCorpusDir, String ocrCorpusDir, String outHtmlFolder,
			String encoding) {
		
		String htmlhead="<html>\n<head>\n<link rel=\"stylesheet\" "
				+ "type=\"text/css\" href=\"style.css\">\n</head>\n<body>\n";
		String htmltail="</br></br>\n"+
				"<strong>Legend - </strong>\n Level of confidence that token is an accurately-transcribed word</br>\n\n"+
				"<span class='rank-13'>&nbsp;&nbsp;&nbsp;</span> extremely low\n"+
				"<span class='rank-7'>&nbsp;&nbsp;&nbsp;</span> very low\n"+
				"<span class='rank-1'>&nbsp;&nbsp;&nbsp;</span> low\n"+
				"<span class='rank0'>&nbsp;&nbsp;&nbsp;</span> undetermined\n"+
				"<span class='rank1'>&nbsp;&nbsp;&nbsp;</span> medium\n"+
				"<span class='rank6'>&nbsp;&nbsp;&nbsp;</span> high\n"+
				"<span class='rank16'>&nbsp;&nbsp;&nbsp;</span> very high</br>\n"+
				"</body>\n</html>\n";
		
		CharacterNgramExtractor charTokenizer=new CharacterNgramExtractor();
		WordLevelTokenizer wordTokenizer=new WordLevelTokenizer();
		
//		for getting probability of n-grams
		CharacterNgramCounterForCorpus ngramCounterForCorpus=new CharacterNgramCounterForCorpus();
		ngramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram, encoding);
		ProbabilityGeneratorFromNgram probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> ngram_prob=probGenerator.getProbability(ngramCounterForCorpus.ngram_freq);	
		
//		for getting probability of (n-1)-grams
		CharacterNgramCounterForCorpus nless1gramCounterForCorpus=new CharacterNgramCounterForCorpus();
		nless1gramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram-1, encoding);
		probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> nless1gram_prob=probGenerator.getProbability(nless1gramCounterForCorpus.ngram_freq);
		
		WordProbability wordprob=new WordProbability(ngram_prob, nless1gram_prob);		

		String[] filenames=new File(ocrCorpusDir).list();
		
		for (String filename : filenames) {
			
			if (!isGoodFileName(filename)) {
				System.out.println("Bad filename "+filename+", skipped");
				continue;
			}else{
				System.out.println("Processing file "+filename);
			}
			
			Document doc=new Document(new File(ocrCorpusDir+File.separator+filename), encoding);
			ArrayList<ArrayList<String>> matrixtokens=wordTokenizer.tokenizerToWordsByLine(doc.text);
		    
		    if (matrixtokens!=null) {
		    	
		    	StringBuilder htmlbody=new StringBuilder();		    			    			    	
		    			    	
		    	for (ArrayList<String> linetokens : matrixtokens) {
		    		
		    		if (linetokens==null)
		    			continue;
	    		
		    		for (String token : linetokens) {
		    			double est=wordprob.estimate(token, ngram);
			    		htmlbody.append("<span class='rank"+roundDecimal(est)+" "+est+"'>"+token+"</span>\n");
		    		}		    				    		
		    		
		    		htmlbody.append("</br>\n");
		    	}		    
		    	
		    	String outFileDir=outHtmlFolder+File.separator+filename;
		    	FileIO.writeFile(outFileDir+".html", htmlhead+htmlbody.toString()+htmltail);
		    }
		    
			
		}
		
	}
	
	/***
	 * this is actually for part of the html code, collaborating with Jason Best
	 * to be applied on CSS for highlighting different OCR confidence scores
	 * 
	 * UNFISHED, I want to combine scores from different n-grams, e.g. tri-gram and bi-gram
	 * 
	 * @param ngram
	 * @param trainCorpusDir
	 * @param ocrCorpusDir
	 * @param outHtmlFolder
	 * @param encoding
	 */
	public void writeHTMLOutputWithCombinedScore (int ngram, String trainCorpusDir, String ocrCorpusDir, String outHtmlFolder,
			String encoding) {
		
		String htmlhead="<html>\n<head>\n<link rel=\"stylesheet\" "
				+ "type=\"text/css\" href=\"style.css\">\n</head>\n<body>\n";
		String htmltail="</body>\n</html>\n";
		
		CharacterNgramExtractor charTokenizer=new CharacterNgramExtractor();
		WordLevelTokenizer wordTokenizer=new WordLevelTokenizer();
		
//		for getting probability of n-grams
		CharacterNgramCounterForCorpus ngramCounterForCorpus=new CharacterNgramCounterForCorpus();
		ngramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram, encoding);
		ProbabilityGeneratorFromNgram probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> ngram_prob=probGenerator.getProbability(ngramCounterForCorpus.ngram_freq);	
		
//		for getting probability of (n-1)-grams
		CharacterNgramCounterForCorpus nless1gramCounterForCorpus=new CharacterNgramCounterForCorpus();
		nless1gramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram-1, encoding);
		probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> nless1gram_prob=probGenerator.getProbability(nless1gramCounterForCorpus.ngram_freq);
		
		WordProbability wordprob=new WordProbability(ngram_prob, nless1gram_prob);		

		String[] filenames=new File(ocrCorpusDir).list();
		
		for (String filename : filenames) {
			
			if (!isGoodFileName(filename)) {
				System.out.println("Bad filename "+filename+", skipped");
				continue;
			}else{
				System.out.println("Processing file "+filename);
			}
			
			Document doc=new Document(new File(ocrCorpusDir+File.separator+filename), encoding);
			ArrayList<ArrayList<String>> matrixtokens=wordTokenizer.tokenizerToWordsByLine(doc.text);
		    
		    if (matrixtokens!=null) {
		    	
		    	StringBuilder htmlbody=new StringBuilder();		    			    			    	
		    			    	
		    	for (ArrayList<String> linetokens : matrixtokens) {
		    		
		    		if (linetokens==null)
		    			continue;
	    		
		    		for (String token : linetokens) {
		    			double est=wordprob.estimate(token, ngram);
			    		htmlbody.append("<span class='rank"+roundDecimal(est)+" "+est+"'>"+token+"</span>\n");
		    		}		    				    		
		    		
		    		htmlbody.append("</br>\n");
		    	}		    
		    	
		    	String outFileDir=outHtmlFolder+File.separator+filename;
		    	FileIO.writeFile(outFileDir+".html", htmlhead+htmlbody.toString()+htmltail);
		    }
		    
			
		}
		
	}
	
	
	
	/***
	 * It outputs word + ocr probability in this way:
	 * word\tprob
	 * 
	 */
	public void writeTextOutput (int ngram, String trainCorpusDir, String ocrCorpusDir, String outFolder,
			String encoding) {
		
		CharacterNgramExtractor charTokenizer=new CharacterNgramExtractor();
		WordLevelTokenizer wordTokenizer=new WordLevelTokenizer();
		
//		for getting probability of n-grams
		CharacterNgramCounterForCorpus ngramCounterForCorpus=new CharacterNgramCounterForCorpus();
		ngramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram, encoding);
		ProbabilityGeneratorFromNgram probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> ngram_prob=probGenerator.getProbability(ngramCounterForCorpus.ngram_freq);	
		
//		for getting probability of (n-1)-grams
		CharacterNgramCounterForCorpus nless1gramCounterForCorpus=new CharacterNgramCounterForCorpus();
		nless1gramCounterForCorpus.countFrequencyInCorpus(trainCorpusDir, charTokenizer, ngram-1, encoding);
		probGenerator=new ProbabilityGeneratorFromNgram();
		HashMap<String,Double> nless1gram_prob=probGenerator.getProbability(nless1gramCounterForCorpus.ngram_freq);
		
		WordProbability wordprob=new WordProbability(ngram_prob, nless1gram_prob);		

		String[] filenames=new File(ocrCorpusDir).list();
		
		for (String filename : filenames) {
			
			if (!isGoodFileName(filename)) {
				System.out.println("Bad filename "+filename+", skipped");
				continue;
			}else{
				System.out.println("Processing file "+filename);
			}
			
			Document doc=new Document(new File(ocrCorpusDir+File.separator+filename), encoding);
			ArrayList<String> tokens=wordTokenizer.tokenizeToWords(doc.text);
					    
		    if (tokens!=null) {
		    	
		    	StringBuilder content=new StringBuilder();
		    			    	
		    	for (String token : tokens) {
		    		
		    		double est=wordprob.estimate(token, ngram);
		    		content.append(token+"\t"+est+"\n");
		    		
		    	}		    
		    	
		    	String outFileDir=outFolder+File.separator+filename;
		    	FileIO.writeFile(outFileDir, content.toString());
		    }
			
			
		}
		
		
	}
	
	
	public static void main (String[] args) {
		
		int outputType=1; //html output
		
		int ngram=3;
		
		String trainCorpusDir="/Users/miaochen/Documents/Projects/iDigBio/data/label-data/herb/gold/ocr";
		String ocrCorpusDir="/Users/miaochen/Documents/Projects/iDigBio/silver_ocr";
//		String outFolderDir="/Users/miaochen/Documents/Projects/iDigBio/herb_silver_trigram_confidence";
		String outHtmlFolder="/Users/miaochen/Documents/Projects/iDigBio/herb_allsilver_trigram_html";
		String encoding="utf-8";
		
		WordProbabilityWriter writer=new WordProbabilityWriter();
		writer.writeHTMLOutput(ngram, trainCorpusDir, ocrCorpusDir, outHtmlFolder, encoding);
		
	}
	

}
