package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class NSN {

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
		int j, k, ell, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		if(m < 2)
			return null;
		
		/* Preprocessing */
		if (x[0] == x[1]) {
			k = 2;
			ell = 1;
		} else {
			k = 1;
			ell = 2;
		}

		/* Searching */
		j = 0;
		while (j <= n - m)
			if (x[1] != y[j + 1])
				j += k;
			else {
				if (arrayCmp(x, 2, y, j + 2, m - 2) == 0 && x[0] == y[j])
					result.add(j);
				j += ell;
			}

		return result;
	}

}
