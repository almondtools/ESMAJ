package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class SMT {

	private static void preBmBc(char[] x, int bmBc[]) {
		int i, m = x.length;

		for (i = 0; i < bmBc.length; ++i)
			bmBc[i] = m;
		for (i = 0; i < m - 1; ++i)
			bmBc[x[i]] = m - i - 1;
	}

	private static void preQsBc(char[] x, int qsBc[]) {
		int i, m = x.length;

		for (i = 0; i < qsBc.length; ++i)
			qsBc[i] = m + 1;
		for (i = 0; i < m; ++i)
			qsBc[x[i]] = m - i;
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

		int[] bmBc = new int[65536];
		int[] qsBc = new int[65536];

		/* Preprocessing */
		preBmBc(x, bmBc);
		preQsBc(x, qsBc);

		/* Searching */
		j = 0;
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			j += Math.max(bmBc[y[j + m - 1]], qsBc[y[j + m]]);
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}
	
	public static SMT compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;

		int[] bmBc = new int[65536];
		int[] qsBc = new int[65536];

		preBmBc(x, bmBc);
		preQsBc(x, qsBc);
		
		SMT smt = new SMT();
		smt.bmBc = bmBc;
		smt.m = m;
		smt.qsBc = qsBc;
		smt.x = x;
		
		return smt;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			j += Math.max(bmBc[y[j + m - 1]], qsBc[y[j + m]]);
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}
	
	private int m;
	private char[] x;
	private int[] bmBc, qsBc;

}
