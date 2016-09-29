package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class BR {

	private static void preBrBc(char[] x, int[][] brBc) {
		int a, b, i, m = x.length;

		for (a = 0; a < brBc.length; ++a)
			for (b = 0; b < brBc.length; ++b)
				brBc[a][b] = m + 2;
		for (a = 0; a < brBc.length; ++a)
			brBc[a][x[0]] = m + 1;
		for (i = 0; i < m - 1; ++i)
			brBc[x[i]][x[i + 1]] = m - i;
		for (a = 0; a < brBc.length; ++a)
			brBc[x[m - 1]][a] = 1;
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

	private static int calculateBrBcSize(char[] x, char[] y) {
		int i;
		char maxChar = 0;
		for (i = 0; i < x.length; i++)
			if (x[i] > maxChar)
				maxChar = x[i];
		for (i = 0; i < y.length; i++)
			if (y[i] > maxChar)
				maxChar = y[i];
		maxChar++;
		return maxChar;
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), src = source.toCharArray();
		char[] y = new char[src.length + 2];
		System.arraycopy(src, 0, y, 0, src.length);
		int j, m = x.length, n = src.length;
		List<Integer> result = new ArrayList<Integer>();

		if(calculateBrBcSize(x, src) > 256)
			return null;
		
		int[][] brBc = new int[256][256];

		/* Preprocessing */
		preBrBc(x, brBc);

		/* Searching */
		j = 0;
		y[n + 1] = '\0';
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			j += brBc[y[j + m]][y[j + m + 1]];
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}
	
	public static BR compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;
		
		int[][] brBc = new int[256][256];

		preBrBc(x, brBc);
		
		BR br = new BR();
		br.brBc = brBc;
		br.m = m;
		br.x = x;
		
		return br;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] src = source.toCharArray();
		char[] y = new char[src.length + 2];
		System.arraycopy(src, 0, y, 0, src.length);
		int j, n = src.length;
		List<Integer> result = new ArrayList<Integer>();

		if(calculateBrBcSize(x, src) > 256)
			return null;
		
		j = 0;
		y[n + 1] = '\0';
		while (j < n - m) {
			if (arrayCmp(x, 0, y, j, m) == 0)
				result.add(j);
			j += brBc[y[j + m]][y[j + m + 1]];
		}
		if(j == n - m && arrayCmp(x, 0, y, j, m) == 0)
			result.add(j);

		return result;
	}
	
	private char[] x;
	private int m;
	private int[][] brBc;

}
