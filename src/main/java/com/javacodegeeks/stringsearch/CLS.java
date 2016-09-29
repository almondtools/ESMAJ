package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class CLS {

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
		for (i = 0; i < m; i++)
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
		int i, j, last, nd, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] h = new int[x.length];
		int[] next = new int[x.length];
		int[] shift = new int[x.length];

		/* Processing */
		nd = preColussi(x, h, next, shift);

		/* Searching */
		i = j = 0;
		last = -1;
		while (j <= n - m) {
			while (i < m && last < j + h[i] && x[h[i]] == y[j + h[i]])
				i++;
			if (i >= m || last >= j + h[i]) {
				result.add(j);
				i = m;
			}
			if (i > nd)
				last = j + m - 1;
			j += shift[i];
			i = next[i];
		}

		return result;
	}
	
	public static CLS compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int nd, m = ptrn.length;

		int[] h = new int[x.length];
		int[] next = new int[x.length];
		int[] shift = new int[x.length];

		/* Processing */
		nd = preColussi(x, h, next, shift);
		
		CLS cls = new CLS();
		cls.x = x;
		cls.h = h;
		cls.m = m;
		cls.next = next;
		cls.shift = shift;
		cls.nd = nd;
		
		return cls;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, last, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		i = j = 0;
		last = -1;
		while (j <= n - m) {
			while (i < m && last < j + h[i] && x[h[i]] == y[j + h[i]])
				i++;
			if (i >= m || last >= j + h[i]) {
				result.add(j);
				i = m;
			}
			if (i > nd)
				last = j + m - 1;
			j += shift[i];
			i = next[i];
		}

		return result;
	}
	
	private char[] x;
	private int[] h, next, shift;
	private int nd, m;
	
}
