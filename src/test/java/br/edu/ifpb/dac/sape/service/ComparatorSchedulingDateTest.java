package br.edu.ifpb.dac.sape.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * 
 * Test class created to test the ComparatorSchedullingDate class
 * 
 * 
 *	@author ytallo
 *
 */
public class ComparatorSchedulingDateTest {
	
	
	private static ComparatorSchedulingDate comparator;
	
	private static Scheduling schedul1;
	
	private static Scheduling schedul2;
	
	@BeforeAll
	public static void setUp() {
		
		comparator = new ComparatorSchedulingDate();
		
		schedul1 = new Scheduling();
		
		schedul2 = new Scheduling();
	}
	
	/**
	 * if the date of schedul1 is before schedul2 the method returns -1
	 */
	@Test
	public void testCompareIsBefore() {
		
		LocalDate date1 = LocalDate.of(2023, 4, 5);
		schedul1.setScheduledDate(date1);
		
		LocalDate date2 = LocalDate.of(2023, 4, 6);
		schedul2.setScheduledDate(date2);
		
		assertEquals(-1,comparator.compare(schedul1, schedul2));
	}
	
	/**
	 * if the date of schedull1 is after the date of schedull2 the method returns 1
	 */
	
	@Test
	public void testCompareIsAfter() {
		
		LocalDate date1 = LocalDate.of(2023, 4, 9);
		schedul1.setScheduledDate(date1);
		
		LocalDate date2 = LocalDate.of(2023, 4, 7);
		schedul2.setScheduledDate(date2);
		
		assertEquals(1,comparator.compare(schedul1, schedul2));
	}
	/**
	 * if both schedules have the same date the method return is 0
	 */
	@Test
	public void testCompareIsEqual() {
		
		LocalDate date1 = LocalDate.of(2023, 4, 9);
		schedul1.setScheduledDate(date1);
		
		LocalDate date2 = LocalDate.of(2023, 4, 9);
		schedul2.setScheduledDate(date2);
		
		assertEquals(0,comparator.compare(schedul1, schedul2));
	}
}