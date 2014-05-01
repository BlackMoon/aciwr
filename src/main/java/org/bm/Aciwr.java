/**
 * 
 */
package org.bm;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

/**
 * @author Black Moon
 *
 */
public class Aciwr {
	static final Logger log = Logger.getLogger(Aciwr.class);
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("Start");
		
		Processor p = new Processor();
		
		Collection<String> words = p.GenerateWords();		
		Iterator<String> it = words.iterator();
		
		String word;
		Integer cnt, min = 0;
		
		Map<String, Integer> entries = new HashMap<String, Integer>(),			// word entries
							 sorted = new HashMap<String, Integer>();
		
		while(it.hasNext()){			
			word = it.next();
			
			cnt = new Integer(1);
			if (entries.containsKey(word))
				cnt = entries.get(word) + 1;
			
			entries.put(word, cnt);				
			
			// fill sorted Map
			if (sorted.size() < p.FrequentSize){
				sorted.put(word, cnt);
				
				min = Collections.min(sorted.values());
			}
			else {
				if (cnt > min) {
					sorted = MapUtil.sortByValue(sorted);
					
					if (!sorted.containsKey(word))
					{	
						// remove first entry
						Iterator<Entry<String, Integer>> it_first = sorted.entrySet().iterator();
						it_first.next();
						it_first.remove(); 
					}
					
					sorted.put(word, cnt);
					min = Collections.min(sorted.values());
				}
			}			
		}
		
		for (Entry<String, Integer> e: MapUtil.sortByValue(sorted).entrySet()){
			System.out.println(e.getKey() + " - " + e.getValue());
		}
		
		System.exit(0);
	}

}
