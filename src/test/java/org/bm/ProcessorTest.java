/**
 * 
 */
package org.bm;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Black Moon
 *
 */
public class ProcessorTest {
	
	@Test
    public void testGenerateWords(){
		Processor p = new Processor();		
		Assert.assertNotNull(p);
		
		Collection<String> words = p.GenerateWords();		
		for(Iterator<String> it = words.iterator(); it.hasNext();){
			Assert.assertNotNull(it.next());
		}        
	}
}
