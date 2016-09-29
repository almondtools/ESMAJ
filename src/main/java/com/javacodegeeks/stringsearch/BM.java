package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class BM {

	private static void preBmBc(char[] x, int bmBc[]) {
		int i, m = x.length;

		for (i = 0; i < bmBc.length; ++i)
			bmBc[i] = m;
		for (i = 0; i < m - 1; ++i)
			bmBc[x[i]] = m - i - 1;
	}

	private static void suffixes(char[] x, int[] suff) {
		int f = 0, g, i, m = x.length;

		suff[m - 1] = m;
		g = m - 1;
		for (i = m - 2; i >= 0; --i) {
			if (i > g && suff[i + m - 1 - f] < i - g)
				suff[i] = suff[i + m - 1 - f];
			else {
				if (i < g)
					g = i;
				f = i;
				while (g >= 0 && x[g] == x[g + m - 1 - f])
					--g;
				suff[i] = f - g;
			}
		}
	}

	private static void preBmGs(char[] x, int bmGs[]) {
		int i, j, m = x.length;
		int[] suff = new int[m];

		suffixes(x, suff);

		for (i = 0; i < m; ++i)
			bmGs[i] = m;
		j = 0;
		for (i = m - 1; i >= 0; --i)
			if (suff[i] == i + 1)
				for (; j < m - 1 - i; ++j)
					if (bmGs[j] == m)
						bmGs[j] = m - 1 - i;
		for (i = 0; i <= m - 2; ++i)
			bmGs[m - 1 - suff[i]] = m - 1 - i;
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		int[] bmGs = new int[m];
		int[] bmBc = new int[65536];

		/* Preprocessing */
		preBmGs(x, bmGs);
		preBmBc(x, bmBc);

		/* Searching */
		j = 0;
		while (j <= n - m) {
			for (i = m - 1; i >= 0 && x[i] == y[i + j]; --i)
				;
			if (i < 0) {
				result.add(j);
				j += bmGs[0];
			} else
				j += Math.max(bmGs[i], bmBc[y[i + j]] - m + 1 + i);
		}
		
		return result;
	}
	
	public static BM compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;
		
		int[] bmGs = new int[m];
		int[] bmBc = new int[65536];

		preBmGs(x, bmGs);
		preBmBc(x, bmBc);
		
		BM bm = new BM();
		bm.bmBc = bmBc;
		bm.bmGs = bmGs;
		bm.x = x;
		bm.m = m;
		
		return bm;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j <= n - m) {
			for (i = m - 1; i >= 0 && x[i] == y[i + j]; --i)
				;
			if (i < 0) {
				result.add(j);
				j += bmGs[0];
			} else
				j += Math.max(bmGs[i], bmBc[y[i + j]] - m + 1 + i);
		}
		
		return result;
	}
	
	private int[] bmGs, bmBc;
	private char[] x;
	private int m;

}
