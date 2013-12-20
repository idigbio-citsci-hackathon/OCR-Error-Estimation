package edu.uf.idigbio;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class FileIO {
	
	/**
	 * Read a file to string, given its encoding information
	 * 
	 * @param filePath
	 *            input file path
	 * @param encoding
	 *            encoding
	 * @return the string derived from input file
	 * @throws IOException
	 *             throw exception
	 * @throws FileNotFoundException
	 *             throw exception
	 */
	public static String getFileByEncoding(String filePath, String encoding) {
		InputStreamReader isr=null;
		try {
			isr = new InputStreamReader(new FileInputStream(
					filePath), encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		BufferedReader in = new BufferedReader(isr);
		String s;
		try {
			while ((s = in.readLine()) != null) {
				sb.append(s);
				sb.append("\n");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static String getFileObjectAsString(File afile, String encoding) {
		StringBuffer sb=new StringBuffer();
		try{
			BufferedReader br=new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(afile)), encoding));			
			String strLine;
			while((strLine=br.readLine())!=null){
				sb.append(strLine);
				sb.append("\r\n");
			}				
			br.close();
		}catch (Exception e){
			System.err.println("Error: "+e.getMessage());
		}
		return sb.toString();
	}
	
	public static void writeFile(String filePath,String content){
		try{
			FileWriter fw=new FileWriter(filePath);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			fw.close();
		}catch (Exception e){
			System.err.println("Error: "+e.getMessage());
		}
	}
	
	/***
	 * 
	 * @param filePath
	 * @param keyType  value type of the hashmap (Double, String, Integer...), usually the key type is String
	 *        valueType is usually double
	 *        keytype: 0=Double
	 *               1=Integer
	 * @return
	 * @throws IOException 
	 */
	public static HashMap readFileToHash_StringDouble(String filePath) {
		
		HashMap k_v = null;

		k_v = new HashMap<String, Double>();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new DataInputStream(new FileInputStream(filePath))));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] units = strLine.trim().split("\\t");
				if (units.length == 2) {
					// we very simply check this is a decimal number
					if (!units[1].matches("^[0-9]*(\\.[0-9]+)?$")) {
						continue;
					}
					k_v.put(units[0], Double.valueOf(units[1]));
				}
			}
			br.close();
		} catch (NumberFormatException e) {
			// this exception is usually "String-like" values, so we just
			// discard it
			e.printStackTrace();
		} catch (IOException e) {
			// this exception is usually "String-like" values, so we just
			// discard it
			e.printStackTrace();
		}

		return k_v;
	}
	
	
	public static void main (String[] args) {
		
		
		
	}

}
