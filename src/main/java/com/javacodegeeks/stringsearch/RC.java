package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class RC {

	private static void preRc(char[] x, int[] h, int[][] rcBc, int[] rcGs) {
		int a, i, j, k, q, r = 0, s, m = (x.length - 1);

		int[] hmin = new int[x.length];
		int[] kmin = new int[x.length];
		int[] link = new int[x.length];
		int[] locc = new int[rcBc.length];
		int[] rmin = new int[x.length];

		/* Computation of link and locc */
		for (a = 0; a < locc.length; ++a)
			locc[a] = -1;
		link[0] = -1;
		for (i = 0; i < m - 1; ++i) {
			link[i + 1] = locc[x[i]];
			locc[x[i]] = i;
		}

		/* Computation of rcBc */
		for (a = 0; a < locc.length; ++a)
			for (s = 1; s <= m; ++s) {
				i = locc[a];
				j = link[m - s];
				while (i - j != s && j >= 0)
					if (i - j > s)
						i = link[i + 1];
					else
						j = link[j + 1];
				while (i - j > s)
					i = link[i + 1];
				rcBc[a][s] = m - i - 1;
			}

		/* Computation of hmin */
		k = 1;
		i = m - 1;
		while (k <= m) {
			while (i - k >= 0 && x[i - k] == x[i])
				--i;
			hmin[k] = i;
			q = k + 1;
			while (hmin[q - k] - (q - k) > i) {
				hmin[q] = hmin[q - k];
				++q;
			}
			i += (q - k);
			k = q;
			if (i == m)
				i = m - 1;
		}

		/* Computation of kmin */
		for (i = 0; i < m; i++)
			kmin[i] = 0;
		for (k = m; k > 0; --k)
			kmin[hmin[k]] = k;

		/* Computation of rmin */
		for (i = m - 1; i >= 0; --i) {
			if (hmin[i + 1] == i)
				r = i + 1;
			rmin[i] = r;
		}

		/* Computation of rcGs */
		i = 1;
		for (k = 1; k <= m; ++k)
			if (hmin[k] != m - 1 && kmin[hmin[k]] == k) {
				h[i] = hmin[k];
				rcGs[i++] = k;
			}
		i = m - 1;
		for (j = m - 2; j >= 0; --j)
			if (kmin[j] == 0) {
				h[i] = j;
				rcGs[i--] = rmin[j];
			}
		rcGs[m] = rmin[0];
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, j, s, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[][] rcBc = new int[65536][x.length];
		int[] rcGs = new int[x.length];
		int[] h = new int[x.length];

		/* Preprocessing */
		preRc(x, h, rcBc, rcGs);

		/* Searching */
		j = 0;
		s = m;
		while (j <= n - m) {
			while (j <= n - m && x[m - 1] != y[j + m - 1]) {
				s = rcBc[y[j + m - 1]][s];
				j += s;
			}
			for (i = 1; i < m && j + h[i] < n && x[h[i]] == y[j + h[i]]; ++i)
				;
			if (j <= n - m && i >= m)
				result.add(j);
			s = rcGs[i];
			j += s;
		}

		return result;
	}
	
	public static RC compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int m = ptrn.length;

		int[][] rcBc = new int[65536][x.length];
		int[] rcGs = new int[x.length];
		int[] h = new int[x.length];

		preRc(x, h, rcBc, rcGs);
		
		RC rc = new RC();
		rc.h = h;
		rc.m = m;
		rc.rcBc = rcBc;
		rc.rcGs = rcGs;
		rc.x = x;
		
		return rc;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, s, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		s = m;
		while (j <= n - m) {
			while (j <= n - m && x[m - 1] != y[j + m - 1]) {
				s = rcBc[y[j + m - 1]][s];
				j += s;
			}
			for (i = 1; i < m && j + h[i] < n && x[h[i]] == y[j + h[i]]; ++i)
				;
			if (j <= n - m && i >= m)
				result.add(j);
			s = rcGs[i];
			j += s;
		}

		return result;
	}
	
	private char[] x;
	private int m;
	private int[] rcGs, h;
	private int[][] rcBc;

}
