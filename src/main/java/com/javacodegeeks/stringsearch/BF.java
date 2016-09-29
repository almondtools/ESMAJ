package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class BF {

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		/* Searching */
		for (j = 0; j <= n - m; ++j) {
			for (i = 0; i < m && x[i] == y[i + j]; ++i)
				;
			if (i >= m)
				result.add(j);
		}

		return result;
	}
	
}
