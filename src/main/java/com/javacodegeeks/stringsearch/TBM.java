package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class TBM {

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
		int bcShift, i, j, shift, u, v, turboShift, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] bmGs = new int[m];
		int[] bmBc = new int[65536];

		/* Preprocessing */
		preBmGs(x, bmGs);
		preBmBc(x, bmBc);

		/* Searching */
		j = u = 0;
		shift = m;
		while (j <= n - m) {
			i = m - 1;
			while (i >= 0 && x[i] == y[i + j]) {
				--i;
				if (u != 0 && i == m - 1 - shift)
					i -= u;
			}
			if (i < 0) {
				result.add(j);
				shift = bmGs[0];
				u = m - shift;
			} else {
				v = m - 1 - i;
				turboShift = u - v;
				bcShift = bmBc[y[i + j]] - m + 1 + i;
				shift = Math.max(turboShift, bcShift);
				shift = Math.max(shift, bmGs[i]);
				if (shift == bmGs[i])
					u = Math.min(m - shift, v);
				else {
					if (turboShift < bcShift)
						shift = Math.max(shift, u + 1);
					u = 0;
				}
			}
			j += shift;
		}

		return result;
	}
	
	public static TBM compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;

		int[] bmGs = new int[m];
		int[] bmBc = new int[65536];

		preBmGs(x, bmGs);
		preBmBc(x, bmBc);
		
		TBM tbm = new TBM();
		tbm.m = m;
		tbm.bmBc = bmBc;
		tbm.bmGs = bmGs;
		tbm.x = x;
		
		return tbm;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int bcShift, i, j, shift, u, v, turboShift, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		j = u = 0;
		shift = m;
		while (j <= n - m) {
			i = m - 1;
			while (i >= 0 && x[i] == y[i + j]) {
				--i;
				if (u != 0 && i == m - 1 - shift)
					i -= u;
			}
			if (i < 0) {
				result.add(j);
				shift = bmGs[0];
				u = m - shift;
			} else {
				v = m - 1 - i;
				turboShift = u - v;
				bcShift = bmBc[y[i + j]] - m + 1 + i;
				shift = Math.max(turboShift, bcShift);
				shift = Math.max(shift, bmGs[i]);
				if (shift == bmGs[i])
					u = Math.min(m - shift, v);
				else {
					if (turboShift < bcShift)
						shift = Math.max(shift, u + 1);
					u = 0;
				}
			}
			j += shift;
		}

		return result;
	}
	
	private int m;
	private int[] bmGs, bmBc;
	private char[] x;

}
