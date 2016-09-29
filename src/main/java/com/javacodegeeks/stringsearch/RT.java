package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class RT {

	private static void preBmBc(char[] x, int bmBc[]) {
		int i, m = x.length;

		for (i = 0; i < bmBc.length; ++i)
			bmBc[i] = m;
		for (i = 0; i < m - 1; ++i)
			bmBc[x[i]] = m - i - 1;
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
		int j, m = x.length, n = y.length;
		char c, firstCh, middleCh, lastCh;
		char[] secondCh = new char[m - 1];
		System.arraycopy(x, 1, secondCh, 0, (m - 1));
		List<Integer> result = new ArrayList<Integer>();

		int[] bmBc = new int[65536];

		/* Preprocessing */
		preBmBc(x, bmBc);
		firstCh = x[0];
		middleCh = x[m / 2];
		lastCh = x[m - 1];

		/* Searching */
		j = 0;
		while (j <= n - m) {
			c = y[j + m - 1];
			if (lastCh == c && middleCh == y[j + m / 2] && firstCh == y[j]
					&& arrayCmp(secondCh, 0, y, (j + 1), (m - 2)) == 0)
				result.add(j);
			j += bmBc[c];
		}

		return result;
	}

	public static RT compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;
		char firstCh, middleCh, lastCh;

		int[] bmBc = new int[65536];

		preBmBc(x, bmBc);
		firstCh = x[0];
		middleCh = x[m / 2];
		lastCh = x[m - 1];
		
		RT rt = new RT();
		rt.firstCh = firstCh;
		rt.lastCh = lastCh;
		rt.middleCh = middleCh;
		rt.m = m;
		rt.x = x;
		rt.bmBc = bmBc;
		
		return rt;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, n = y.length;
		char c;
		char[] secondCh = new char[m - 1];
		System.arraycopy(x, 1, secondCh, 0, (m - 1));
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j <= n - m) {
			c = y[j + m - 1];
			if (lastCh == c && middleCh == y[j + m / 2] && firstCh == y[j]
					&& arrayCmp(secondCh, 0, y, (j + 1), (m - 2)) == 0)
				result.add(j);
			j += bmBc[c];
		}

		return result;
	}
	
	private int m;
	private int[] bmBc;
	private char firstCh, middleCh, lastCh;
	private char[] x;
	
}
