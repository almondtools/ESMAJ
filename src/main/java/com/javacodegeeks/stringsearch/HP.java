package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class HP {

	private static void preBmBc(char[] x, int bmBc[]) {
		int i, m = x.length;

		for (i = 0; i < bmBc.length; ++i)
			bmBc[i] = m;
		for (i = 0; i < m - 1; ++i)
			bmBc[x[i]] = m - i - 1;
	}

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
		int j, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		char c;

		int[] bmBc = new int[65536];

		/* Preprocessing */
		preBmBc(x, bmBc);

		/* Searching */
		j = 0;
		while (j <= n - m) {
			c = y[j + m - 1];
			if (x[m - 1] == c && arrayCmp(x, 0, y, j, (m - 1)) == 0)
				result.add(j);
			j += bmBc[c];
		}

		return result;
	}
	
	public static HP compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;

		int[] bmBc = new int[65536];

		preBmBc(x, bmBc);
		
		HP hp = new HP();
		hp.x = x;
		hp.bmBc = bmBc;
		hp.m = m;
		
		return hp;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, n = y.length;
		char c;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j <= n - m) {
			c = y[j + m - 1];
			if (x[m - 1] == c && arrayCmp(x, 0, y, j, (m - 1)) == 0)
				result.add(j);
			j += bmBc[c];
		}

		return result;
	}
	
	private char[] x;
	private int m;
	private int[] bmBc;

}
