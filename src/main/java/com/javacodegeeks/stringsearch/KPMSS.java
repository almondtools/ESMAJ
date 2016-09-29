package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class KPMSS {

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

	private static void preKmp(char[] x, int[] kmpNext) {
		int i, j, m = (x.length - 1);

		i = 0;
		j = kmpNext[0] = -1;
		while (i < m) {
			while (j > -1 && x[i] != x[j])
				j = kmpNext[j];
			i++;
			j++;
			if (x[i] == x[j])
				kmpNext[i] = kmpNext[j];
			else
				kmpNext[i] = j;
		}
	}

	private static int attempt(char[] y, char[] x, int start, int wall) {
		int k, m = (x.length - 1);

		k = wall - start;
		while (k < m && x[k] == y[k + start])
			++k;
		return (k);
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, j, k, kmpStart, per, start, wall, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] kmpNext = new int[x.length];
		int[] list = new int[x.length];
		int[] mpNext = new int[x.length];
		int[] z = new int[65536];

		/* Preprocessing */
		preMp(x, mpNext);
		preKmp(x, kmpNext);

		for (i = 0; i < z.length; i++)
			z[i] = -1;
		for (i = 0; i < m; i++)
			list[i] = -1;
		z[x[0]] = 0;
		for (i = 1; i < m; ++i) {
			list[i] = z[x[i]];
			z[x[i]] = i;
		}

		/* Searching */
		wall = 0;
		per = m - kmpNext[m];
		i = j = -1;
		do {
			j += m;
		} while (j < n && z[y[j]] < 0);
		if (j >= n)
			return result;
		i = z[y[j]];
		start = j - i;
		while (start <= n - m) {
			if (start > wall)
				wall = start;
			k = attempt(y, x, start, wall);
			wall = start + k;
			if (k == m) {
				result.add(start);
				i -= per;
			} else
				i = list[i];
			if (i < 0) {
				do {
					j += m;
				} while (j < n && z[y[j]] < 0);
				if (j >= n)
					return result;
				i = z[y[j]];
			}
			kmpStart = start + k - kmpNext[k];
			k = kmpNext[k];
			start = j - i;
			while (start < kmpStart || (kmpStart < start && start < wall)) {
				if (start < kmpStart) {
					i = list[i];
					if (i < 0) {
						do {
							j += m;
						} while (j < n && z[y[j]] < 0);
						if (j >= n)
							return result;
						i = z[y[j]];
					}
					start = j - i;
				} else {
					kmpStart += (k - mpNext[k]);
					k = mpNext[k];
				}
			}
		}

		return result;
	}
	
	public static KPMSS compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, m = ptrn.length;

		int[] kmpNext = new int[x.length];
		int[] list = new int[x.length];
		int[] mpNext = new int[x.length];
		int[] z = new int[65536];

		preMp(x, mpNext);
		preKmp(x, kmpNext);

		for (i = 0; i < z.length; i++)
			z[i] = -1;
		for (i = 0; i < m; i++)
			list[i] = -1;
		z[x[0]] = 0;
		for (i = 1; i < m; ++i) {
			list[i] = z[x[i]];
			z[x[i]] = i;
		}
		
		KPMSS kpmss = new KPMSS();
		kpmss.x = x;
		kpmss.m = m;
		kpmss.kmpNext = kmpNext;
		kpmss.list = list;
		kpmss.mpNext = mpNext;
		kpmss.z = z;
		
		return kpmss;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, k, kmpStart, per, start, wall, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		wall = 0;
		per = m - kmpNext[m];
		i = j = -1;
		do {
			j += m;
		} while (j < n && z[y[j]] < 0);
		if (j >= n)
			return result;
		i = z[y[j]];
		start = j - i;
		while (start <= n - m) {
			if (start > wall)
				wall = start;
			k = attempt(y, x, start, wall);
			wall = start + k;
			if (k == m) {
				result.add(start);
				i -= per;
			} else
				i = list[i];
			if (i < 0) {
				do {
					j += m;
				} while (j < n && z[y[j]] < 0);
				if (j >= n)
					return result;
				i = z[y[j]];
			}
			kmpStart = start + k - kmpNext[k];
			k = kmpNext[k];
			start = j - i;
			while (start < kmpStart || (kmpStart < start && start < wall)) {
				if (start < kmpStart) {
					i = list[i];
					if (i < 0) {
						do {
							j += m;
						} while (j < n && z[y[j]] < 0);
						if (j >= n)
							return result;
						i = z[y[j]];
					}
					start = j - i;
				} else {
					kmpStart += (k - mpNext[k]);
					k = mpNext[k];
				}
			}
		}

		return result;
	}
	
	private char[] x;
	private int m;
	private int[] kmpNext, list, mpNext, z;

}
