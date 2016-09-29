package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class AG {

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

	private static void preBmGs(char[] x, int bmGs[], int[] suff) {
		int i, j, m = x.length;

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

	private static void arrayCpy(int[] trg, int trgIdx, int[] src, int srcIdx,
			int length) {
		for (int i = 0; i < length; i++)
			trg[trgIdx + i] = src[srcIdx + i];
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, k, s, shift, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] bmGs = new int[m];
		int[] skip = new int[m];
		int[] suff = new int[m];
		int[] bmBc = new int[65536];

		/* Preprocessing */
		preBmGs(x, bmGs, suff);
		preBmBc(x, bmBc);

		for (i = 0; i < m; i++)
			skip[i] = 0;

		/* Searching */
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			while (i >= 0) {
				k = skip[i];
				s = suff[i];
				if (k > 0)
					if (k > s) {
						if (i + 1 == s)
							i = (-1);
						else
							i -= s;
						break;
					} else {
						i -= k;
						if (k < s)
							break;
					}
				else {
					if (x[i] == y[i + j])
						--i;
					else
						break;
				}
			}
			if (i < 0) {
				result.add(j);
				skip[m - 1] = m;
				shift = bmGs[0];
			} else {
				skip[m - 1] = m - 1 - i;
				shift = Math.max(bmGs[i], bmBc[y[i + j]] - m + 1 + i);
			}
			j += shift;
			arrayCpy(skip, 0, skip, shift, (m - shift));
			for (i = 0; i < shift; i++)
				skip[m - shift + i] = 0;
		}

		return result;
	}

	public static AG compile(String pattern) {
		char[] x = pattern.toCharArray();
		int i, m = x.length;

		int[] bmGs = new int[m];
		int[] skip = new int[m];
		int[] suff = new int[m];
		int[] bmBc = new int[65536];

		preBmGs(x, bmGs, suff);
		preBmBc(x, bmBc);

		for (i = 0; i < m; i++)
			skip[i] = 0;

		AG ag = new AG();
		ag.bmGs = bmGs;
		ag.skip = skip;
		ag.suff = suff;
		ag.bmBc = bmBc;
		ag.x = x;
		ag.m = m;

		return ag;
	}

	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, k, s, shift, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		j = 0;
		while (j <= n - m) {
			i = m - 1;
			while (i >= 0) {
				k = skip[i];
				s = suff[i];
				if (k > 0)
					if (k > s) {
						if (i + 1 == s)
							i = (-1);
						else
							i -= s;
						break;
					} else {
						i -= k;
						if (k < s)
							break;
					}
				else {
					if (x[i] == y[i + j])
						--i;
					else
						break;
				}
			}
			if (i < 0) {
				result.add(j);
				skip[m - 1] = m;
				shift = bmGs[0];
			} else {
				skip[m - 1] = m - 1 - i;
				shift = Math.max(bmGs[i], bmBc[y[i + j]] - m + 1 + i);
			}
			j += shift;
			arrayCpy(skip, 0, skip, shift, (m - shift));
			for (i = 0; i < shift; i++)
				skip[m - shift + i] = 0;
		}

		return result;
	}

	private int[] bmGs, skip, suff, bmBc;
	private char[] x;
	private int m;

}
