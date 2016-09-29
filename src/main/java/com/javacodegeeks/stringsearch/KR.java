package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class KR {

	private static int rehash(char a, char b, int h, int d) {
		return ((((h) - (a) * d) << 1) + (b));
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
		int d, hx, hy, i, j, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		if(m > n)
			return null;
		
		/* Preprocessing */
		/*
		 * computes d = 2^(m-1) with the left-shift operator
		 */
		for (d = i = 1; i < m; ++i)
			d = (d << 1);

		for (hy = hx = i = 0; i < m; ++i) {
			hx = ((hx << 1) + x[i]);
			hy = ((hy << 1) + y[i]);
		}

		/* Searching */
		j = 0;
		while (j < n - m) {
			if (hx == hy && arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			hy = rehash(y[j], y[j + m], hy, d);
			++j;
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}

}
