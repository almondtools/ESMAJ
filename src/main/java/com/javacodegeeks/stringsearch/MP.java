package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class MP {

	private static void preMp(char[] x, int[] mpNext) {
		int i, j, m = (x.length - 1);

		i = 0;
		j = mpNext[0] = -1;
		while (i < m) {
			while (j > -1 && x[i] != x[j])
				j = mpNext[j];
			mpNext[++i] = ++j;
		}
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, j, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] mpNext = new int[x.length];

		/* Preprocessing */
		preMp(x, mpNext);

		/* Searching */
		i = j = 0;
		while (j < n) {
			while (i > -1 && x[i] != y[j])
				i = mpNext[i];
			i++;
			j++;
			if (i >= m) {
				result.add(j - i);
				i = mpNext[i];
			}
		}

		return result;
	}
	
	public static MP compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int m = ptrn.length;

		int[] mpNext = new int[x.length];

		/* Preprocessing */
		preMp(x, mpNext);
		
		MP mp = new MP();
		mp.m = m;
		mp.mpNext = mpNext;
		mp.x = x;
		
		return mp;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		i = j = 0;
		while (j < n) {
			while (i > -1 && x[i] != y[j])
				i = mpNext[i];
			i++;
			j++;
			if (i >= m) {
				result.add(j - i);
				i = mpNext[i];
			}
		}

		return result;
	}
	
	private char[] x;
	private int m;
	private int[] mpNext;

}
