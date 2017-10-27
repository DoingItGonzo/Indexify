import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.text.*;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;



public class Indexing {
	

	@SuppressWarnings("rawtypes")
	public static void main (String[] args) throws IOException {
				
        File inFile = new File("/Users/travispettrey/Documents/TestingDoc.docx");
		String theText = fileInput(inFile);
		fileOutput(dataProcessing(theText));
	}
	

		@SuppressWarnings("resource")
		public static String fileInput(File file) {
			String allText = "";
		        try {
		            FileInputStream fis = new FileInputStream(file.getAbsolutePath());
		            XWPFDocument document = new XWPFDocument(fis);
		            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
		            allText = extractor.getText();
		        } catch (Exception exep) {
		            exep.printStackTrace();
		        }
	            return allText;
		}
	
	
		
	public static String[] dataProcessing(String allText) {
		HashMap<String, ArrayList<Integer>> theIndex  = new HashMap<String, ArrayList<Integer>>();
		
		Integer wordCount = 0;
		
		String oneWord = "";
		final CharacterIterator it = new StringCharacterIterator(allText);
		for(char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
			if (Character.isLetter(c)) {
				oneWord += c;
			}
			if ((!Character.isLetter(c)) && oneWord.length() > 0) {
				wordCount++;
				indexAddition(oneWord, theIndex, wordCount);
				oneWord = "";
			}
			else {
				continue;
			}
		}
		return outputFormating(theIndex);
	}
	
	

	public static void indexAddition (String fileString, HashMap<String, ArrayList<Integer>> theIndex, int wordCount) {
		if (!theIndex.containsKey(fileString)) {
			ArrayList<Integer> insides = new ArrayList<>();
			insides.add(wordCount);
			theIndex.put(fileString, insides);
		} else {
			theIndex.get(fileString).add(wordCount);
		}
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] outputFormating (HashMap<String, ArrayList<Integer>> daIndex) {
		Integer i = 0;
		String[] daOutput = new String[daIndex.size()];
		java.util.Iterator<Entry<String, ArrayList<Integer>>> it = daIndex.entrySet().iterator();
		while (it.hasNext()) {
			String nums = "";
			HashMap.Entry entry = (HashMap.Entry)it.next();
			nums += listToString((ArrayList<Integer>) entry.getValue());
			daOutput[i] = (entry.getKey() + ": " + nums);
			i++;
		}
		return daOutput;
	}
	
	

	public static String listToString (ArrayList<Integer> pageNums) {
		String outputNums = "";
		java.util.Iterator<Integer> it = pageNums.iterator();
		Integer formatControl = 0;
		while (it.hasNext()) {
			String thisNum = it.next().toString();
			outputNums += thisNum;
			formatControl++;
			if (formatControl < pageNums.size())
				outputNums += ", ";
			else {
				outputNums += "\r";
			}
		}
		return outputNums;
	}

	
	
	public static void fileOutput (String[] outputIndex) throws IOException {
		XWPFDocument document = new XWPFDocument();
		XWPFParagraph tmpParagraph = document.createParagraph();
		XWPFRun tmpRun = tmpParagraph.createRun();	
		tmpRun.setFontSize(18);
		for (String word: outputIndex) {
			tmpRun.setText(word);
		}
		File file = new File("/Users/travispettrey/Documents/returndFile.docx");
		document.write(new FileOutputStream(file));
		document.close();
	}
	
}