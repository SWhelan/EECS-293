package eecs293;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * My seven digit id is odd so there is a recursive implementation.
 * My network id, slw96, is even so there is an iterator implementation.
 * 
 * The main method has the examples provided in the homework pdf and a few
 * of my own edge cases.
 * 
 * I decided to accept null lists and return empty lists as the prefix of null things.
 * 
 * 9/8/16
 * @author Sarah Whelan (slw96)
 *
 */
public class LongestPrefixFinder {
	
	public static void main(String[] args) {
		List<Integer> answer = Arrays.asList(1, 2);
		List<Integer> emptyList = new ArrayList<Integer>();
		printTestResult("Recurssion 1", longestPrefix(Arrays.asList(1, 2, 4), Arrays.asList(1, 2, 3), Comparator.<Integer>naturalOrder()), answer);
		printTestResult("Recurssion 2", longestPrefix(Arrays.asList(1, 2), Arrays.asList(2, 1), Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Recurssion 3", longestPrefix(Arrays.asList(1, 2), Arrays.asList(1, 2, 3, 4), Comparator.<Integer>naturalOrder()), answer);
		printTestResult("Recurssion 4", longestPrefix(Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 2, 4), Comparator.<Integer>naturalOrder()), answer);
		printTestResult("Recurssion 5", longestPrefix(emptyList, emptyList, Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Recurssion 6", longestPrefix(emptyList, Arrays.asList(1, 2, 4), Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Recurssion 7", longestPrefix(Arrays.asList(1, 2, 4), emptyList, Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Recurssion 8", longestPrefix(null, emptyList, Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Iterators 1", iteratorsLongestPrefix(Arrays.asList(1, 2, 4), Arrays.asList(1, 2, 3), Comparator.<Integer>naturalOrder()), answer);
		printTestResult("Iterators 2", iteratorsLongestPrefix(Arrays.asList(1, 2), Arrays.asList(2, 1), Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Iterators 3", iteratorsLongestPrefix(Arrays.asList(1, 2), Arrays.asList(1, 2, 3, 4), Comparator.<Integer>naturalOrder()), answer);
		printTestResult("Iterators 4", iteratorsLongestPrefix(Arrays.asList(1, 2, 3, 4), Arrays.asList(1, 2, 4), Comparator.<Integer>naturalOrder()), answer);
		printTestResult("Iterators 5", iteratorsLongestPrefix(emptyList, emptyList, Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Iterators 6", iteratorsLongestPrefix(emptyList, Arrays.asList(1, 2, 4), Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Iterators 7", iteratorsLongestPrefix(Arrays.asList(1, 2, 4), emptyList, Comparator.<Integer>naturalOrder()), emptyList);
		printTestResult("Iterators 8", iteratorsLongestPrefix(null, emptyList, Comparator.<Integer>naturalOrder()), emptyList);
	}
	
	private static <T> void printTestResult(String testName, List<T> provided, List<T> expected) {		
		System.out.println(testName + " - " + (provided.equals(expected) ? "passed" : "FAILED"));
	}
		
	public static <T> List<T> longestPrefix(List<T> a, List<T> b, Comparator<? super T> cmp) {
		if(a == null || b == null || a.isEmpty() || b.isEmpty() || cmp.compare(a.get(0), b.get(0)) != 0) {
			return new ArrayList<T>();
		}
		List<T> prefix = longestPrefix(a.subList(1, a.size()), b.subList(1, b.size()), cmp);
		prefix.add(0, a.get(0));
		return prefix;
	}
	
	public static <T> List<T> iteratorsLongestPrefix(List<T> a, List<T> b, Comparator<? super T> cmp) {
		List<T> result = new ArrayList<T>();
		if(a == null || b == null) {
			return result;
		}
		Iterator<T> iterA = a.iterator();
		Iterator<T> iterB = b.iterator();
		iterA.forEachRemaining(current -> {
			if(iterB.hasNext() && cmp.compare(iterB.next(), current) == 0) {
				result.add(current);
			}
		});
		return result;
	}
}
