package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class SS {

	private static int arrayCmp(char[] a, int aIdx, char[] b, int bIdx,
			int length) {
		int i = 0;

		for (i = 0; i < length && aIdx + i < a.length && bIdx + i < b.length; i++) {
			if (a[aIdx + i] == b[bIdx + i])
				;
			else if (a[aIdx + i] > b[bIdx + i])
				return 1;
			else
				return 2;
		}

		if (i < length)
			if (a.length - aIdx == b.length - bIdx)
				return 0;
			else if (a.length - aIdx > b.length - bIdx)
				return 1;
			else
				return 2;
		else
			return 0;
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, m = x.length, n = y.length;
		Cell ptr;
		Cell[] z = new Cell[65536];
		List<Integer> result = new ArrayList<Integer>();

		/* Preprocessing */
		for (i = 0; i < m; ++i) {
			ptr = new Cell();
			ptr.element = i;
			ptr.next = z[x[i]];
			z[x[i]] = ptr;
		}

		/* Searching */
		for (j = m - 1; j < n; j += m)
			for (ptr = z[y[j]]; ptr != null; ptr = ptr.next)
				if (arrayCmp(x, 0, y, j - ptr.element, m) == 0)
					result.add(j - ptr.element);

		return result;
	}
	
	public static SS compile(String pattern) {
		char[] x = pattern.toCharArray();
		int i, m = x.length;
		Cell ptr;
		Cell[] z = new Cell[65536];

		for (i = 0; i < m; ++i) {
			ptr = new Cell();
			ptr.element = i;
			ptr.next = z[x[i]];
			z[x[i]] = ptr;
		}
		
		SS ss = new SS();
		ss.m = m;
		ss.x = x;
		ss.z = z;
		
		return ss;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, n = y.length;
		Cell ptr;
		List<Integer> result = new ArrayList<Integer>();
		
		for (j = m - 1; j < n; j += m)
			for (ptr = z[y[j]]; ptr != null; ptr = ptr.next)
				if (arrayCmp(x, 0, y, j - ptr.element, m) == 0)
					result.add(j - ptr.element);

		return result;
	}

	private int m;
	private char[] x;
	private Cell[] z;
	
	private static class Cell {
		int element;
		Cell next;
	}

}
