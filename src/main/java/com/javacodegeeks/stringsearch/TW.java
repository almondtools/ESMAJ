package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class TW {

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

	/* Computing of the maximal suffix for <= */
	private static int maxSuf(char[] x, int[] p) {
		int ms, j, k, m = x.length;
		char a, b;

		ms = -1;
		j = 0;
		k = p[0] = 1;
		while (j + k < m) {
			a = x[j + k];
			b = x[ms + k];
			if (a < b) {
				j += k;
				k = 1;
				p[0] = j - ms;
			} else if (a == b)
				if (k != p[0])
					++k;
				else {
					j += p[0];
					k = 1;
				}
			else { /* a > b */
				ms = j;
				j = ms + 1;
				k = p[0] = 1;
			}
		}
		return (ms);
	}

	/* Computing of the maximal suffix for >= */
	private static int maxSufTilde(char[] x, int[] p) {
		int ms, j, k, m = x.length;
		char a, b;

		ms = -1;
		j = 0;
		k = p[0] = 1;
		while (j + k < m) {
			a = x[j + k];
			b = x[ms + k];
			if (a > b) {
				j += k;
				k = 1;
				p[0] = j - ms;
			} else if (a == b)
				if (k != p[0])
					++k;
				else {
					j += p[0];
					k = 1;
				}
			else { /* a < b */
				ms = j;
				j = ms + 1;
				k = p[0] = 1;
			}
		}
		return (ms);
	}

	/* Two Way string matching algorithm. */
	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, ell, memory, per, m = x.length, n = y.length;
		int[] p = new int[1], q = new int[1];
		List<Integer> result = new ArrayList<Integer>();

		/* Preprocessing */
		i = maxSuf(x, p);
		j = maxSufTilde(x, q);
		if (i > j) {
			ell = i;
			per = p[0];
		} else {
			ell = j;
			per = q[0];
		}

		/* Searching */
		if (arrayCmp(x, 0, x, per, ell + 1) == 0) {
			j = 0;
			memory = -1;
			while (j <= n - m) {
				i = Math.max(ell, memory) + 1;
				while (i < m && x[i] == y[i + j])
					++i;
				if (i >= m) {
					i = ell;
					while (i > memory && x[i] == y[i + j])
						--i;
					if (i <= memory)
						result.add(j);
					j += per;
					memory = m - per - 1;
				} else {
					j += (i - ell);
					memory = -1;
				}
			}
		} else {
			per = Math.max(ell + 1, m - ell - 1) + 1;
			j = 0;
			while (j <= n - m) {
				i = ell + 1;
				while (i < m && x[i] == y[i + j])
					++i;
				if (i >= m) {
					i = ell;
					while (i >= 0 && x[i] == y[i + j])
						--i;
					if (i < 0)
						result.add(j);
					j += per;
				} else
					j += (i - ell);
			}
		}

		return result;
	}
	
	public static TW compile(String pattern) {
		char[] x = pattern.toCharArray();
		int i, j, ell, per, m = x.length;
		int[] p = new int[1], q = new int[1];

		i = maxSuf(x, p);
		j = maxSufTilde(x, q);
		if (i > j) {
			ell = i;
			per = p[0];
		} else {
			ell = j;
			per = q[0];
		}
		
		TW tw = new TW();
		tw.ell = ell;
		tw.m = m;
		tw.per = per;
		tw.x = x;
		
		return tw;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, memory, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		if (arrayCmp(x, 0, x, per, ell + 1) == 0) {
			j = 0;
			memory = -1;
			while (j <= n - m) {
				i = Math.max(ell, memory) + 1;
				while (i < m && x[i] == y[i + j])
					++i;
				if (i >= m) {
					i = ell;
					while (i > memory && x[i] == y[i + j])
						--i;
					if (i <= memory)
						result.add(j);
					j += per;
					memory = m - per - 1;
				} else {
					j += (i - ell);
					memory = -1;
				}
			}
		} else {
			per = Math.max(ell + 1, m - ell - 1) + 1;
			j = 0;
			while (j <= n - m) {
				i = ell + 1;
				while (i < m && x[i] == y[i + j])
					++i;
				if (i >= m) {
					i = ell;
					while (i >= 0 && x[i] == y[i + j])
						--i;
					if (i < 0)
						result.add(j);
					j += per;
				} else
					j += (i - ell);
			}
		}

		return result;
	}
	
	private int ell, per, m;
	private char[] x;

}
