package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class GG {

	private static int preColussi(char[] x, int[] h, int[] next, int[] shift) {
		int i, k, nd, q, r = 0, s, m = (x.length - 1);
		int[] hmax = new int[x.length];
		int[] kmin = new int[x.length];
		int[] nhd0 = new int[x.length];
		int[] rmin = new int[x.length];

		/* Computation of hmax */
		i = k = 1;
		do {
			while (x[i] == x[i - k])
				i++;
			hmax[k] = i;
			q = k + 1;
			while (hmax[q - k] + k < i) {
				hmax[q] = hmax[q - k] + k;
				q++;
			}
			k = q;
			if (k == i + 1)
				i = k;
		} while (k <= m);

		/* Computation of kmin */
		for (i = 0; i < kmin.length; i++)
			kmin[i] = 0;
		for (i = m; i >= 1; --i)
			if (hmax[i] < m)
				kmin[hmax[i]] = i;

		/* Computation of rmin */
		for (i = m - 1; i >= 0; --i) {
			if (hmax[i + 1] == m)
				r = i + 1;
			if (kmin[i] == 0)
				rmin[i] = r;
			else
				rmin[i] = 0;
		}

		/* Computation of h */
		s = -1;
		r = m;
		for (i = 0; i < m; ++i)
			if (kmin[i] == 0)
				h[--r] = i;
			else
				h[++s] = i;
		nd = s;

		/* Computation of shift */
		for (i = 0; i <= nd; ++i)
			shift[i] = kmin[h[i]];
		for (i = nd + 1; i < m; ++i)
			shift[i] = rmin[h[i]];
		shift[m] = rmin[0];

		/* Computation of nhd0 */
		s = 0;
		for (i = 0; i < m; ++i) {
			nhd0[i] = s;
			if (kmin[i] > 0)
				++s;
		}

		/* Computation of next */
		for (i = 0; i <= nd; ++i)
			next[i] = nhd0[h[i] - kmin[h[i]]];
		for (i = nd + 1; i < m; ++i)
			next[i] = nhd0[m - rmin[h[i]]];
		next[m] = nhd0[m - rmin[h[m - 1]]];

		return (nd);
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, j, k, ell, last, nd, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] h = new int[x.length];
		int[] next = new int[x.length];
		int[] shift = new int[x.length];

		boolean heavy = false;

		for (ell = 0; x[ell] == x[ell + 1]; ell++)
			;
		if (ell == m - 1)
			/* Searching for a power of a single character */
			for (j = ell = 0; j < n; ++j)
				if (x[0] == y[j]) {
					++ell;
					if (ell >= m)
						result.add(j - m + 1);
				} else
					ell = 0;
		else {
			/* Preprocessing */
			nd = preColussi(x, h, next, shift);

			/* Searching */
			i = j = 0;
			last = -1;
			while (j <= n - m) {
				if (heavy && i == 0) {
					k = last - j + 1;
					while (x[0] == y[j + k])
						k++;
					if (k <= ell || x[ell + 1] != y[j + k]) {
						i = 0;
						j += (k + 1);
						last = j - 1;
					} else {
						i = 1;
						last = j + k;
						j = last - (ell + 1);
					}
					heavy = false;
				} else {
					while (i < m && last < j + h[i] && x[h[i]] == y[j + h[i]])
						++i;
					if (i >= m || last >= j + h[i]) {
						result.add(j);
						i = m;
					}
					if (i > nd)
						last = j + m - 1;
					j += shift[i];
					i = next[i];
				}
				heavy = (j > last ? false : true);
			}
		}

		return result;
	}
	
	public static GG compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int nd, m = ptrn.length;

		int[] h = new int[x.length];
		int[] next = new int[x.length];
		int[] shift = new int[x.length];
		
		nd = preColussi(x, h, next, shift);
		
		GG gg = new GG();
		gg.nd = nd;
		gg.m = m;
		gg.x = x;
		gg.h = h;
		gg.next = next;
		gg.shift = shift;
		
		return gg;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, k, ell, last, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		boolean heavy = false;

		for (ell = 0; x[ell] == x[ell + 1]; ell++)
			;
		if (ell == m - 1)
			/* Searching for a power of a single character */
			for (j = ell = 0; j < n; ++j)
				if (x[0] == y[j]) {
					++ell;
					if (ell >= m)
						result.add(j - m + 1);
				} else
					ell = 0;
		else {

			i = j = 0;
			last = -1;
			while (j <= n - m) {
				if (heavy && i == 0) {
					k = last - j + 1;
					while (x[0] == y[j + k])
						k++;
					if (k <= ell || x[ell + 1] != y[j + k]) {
						i = 0;
						j += (k + 1);
						last = j - 1;
					} else {
						i = 1;
						last = j + k;
						j = last - (ell + 1);
					}
					heavy = false;
				} else {
					while (i < m && last < j + h[i] && x[h[i]] == y[j + h[i]])
						++i;
					if (i >= m || last >= j + h[i]) {
						result.add(j);
						i = m;
					}
					if (i > nd)
						last = j + m - 1;
					j += shift[i];
					i = next[i];
				}
				heavy = (j > last ? false : true);
			}
		}

		return result;
	}
	
	private int nd, m;
	private char[] x;
	private int[] h, shift, next;

}
