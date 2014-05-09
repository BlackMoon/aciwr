/**
 * 
 */
package org.bm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * @author Black Moon
 * Word sequence generator class
 */
public class Processor {
	private static final Logger log = Logger.getLogger(Processor.class);
	
	private static final String CONFIG_FILE 	= "config.properties";
	private static final String FREQUENT_SIZE 	= "frequent.num";
	private static final String LOAD_FACTOR 	= "load.factor";
	
	private static final String CSV_FILE 		= "processor.csv.file";
	private static final String CSV_SEPARATOR	= "processor.csv.separator";
	private static final String DEFAULT_SIZE 	= "processor.size";
	private static final String MODE 			= "processor.mode";
	private static final String WORD_CHARS 		= "processor.word.chars";
	private static final String WORD_MINLENGTH 	= "processor.word.minlength";
	private static final String WORD_MAXLENGTH 	= "processor.word.maxlength";
	
	private Properties props = new Properties();
	
	public int FrequentSize = 10;
	public float LoadFactor = 0.75f;
	
	public Processor() {
		try {
			
			props.load(this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE));			
			
			FrequentSize = Integer.parseInt(props.getProperty(FREQUENT_SIZE, "10"));
			LoadFactor = Float.parseFloat(props.getProperty(LOAD_FACTOR, "0.75"));
		} 
		catch (NullPointerException e) {			
			log.warn("Config file not found. Use defaults", e);
		}		
		catch (Exception e) 
		{
			log.warn("Cant't read config properties", e);
		}
	}
	
	/*
	 * generate/read word collection
	 */
	public Collection<String> GenerateWords(){
		
		List<String> words = null;
		
		String mode = props.getProperty(MODE, "auto");
		log.info("Mode: " + mode);
		
		if (mode.equals("auto"))
		{	
			log.info("Random generating...");
			
			int sz = Integer.parseInt(props.getProperty(DEFAULT_SIZE, "10000")),
				word_maxlength = Integer.parseInt(props.getProperty(WORD_MAXLENGTH, "6")),
				word_minlength = Integer.parseInt(props.getProperty(WORD_MINLENGTH, "1"));
			
			// check with 0
			if (word_minlength == 0){
				word_minlength = 1;
				log.warn("word.min.length param is 0. Use 1 instead");
			}
			
			String chars = props.getProperty(WORD_CHARS, "abcdef"), w;  
			Random rm = new Random();
			
			words = new ArrayList<String>(sz);
			for (int i = 0; i < sz; ++i){
				
				w = RandomStringUtils.random(Math.max(word_minlength, rm.nextInt(word_maxlength)), chars);
				words.add(w);
			}
			
		}
		else if (mode.equals("file"))
		{
			log.info("Read from input file...");
			
			BufferedReader br = null;
			
			String csvfile = props.getProperty(CSV_FILE),
				   csvsep = props.getProperty(CSV_SEPARATOR, ";"),
				   line = "";
			
			words = new ArrayList<String>(1000);
			
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(csvfile), "UTF-8"));
				
				while ((line = br.readLine()) != null) {
					
					String[] splits = line.split(csvsep);
					
					for (String s: splits){						
						if (s.length() > 0)
							words.add(s);
					}
				}				
			} 
			catch (FileNotFoundException e) {			
				log.warn("File not found: " + csvfile, e);				
			} 
			catch (IOException e) { 
				log.warn("Can't read input file: " + csvfile, e);
			}
			finally {
				
				if (br != null) {
					try {
						br.close();
					} 
					catch (IOException e) {
						e.printStackTrace();
					}
				}
			}				
		}		
		
		log.info("Total words: " + words.size());		
		return words;
	}
}
