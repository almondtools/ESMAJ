package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class ZT {

	private static void preZtBc(char[] x, int[][] ztBc) {
		int i, j, m = x.length, z = ztBc.length;

		for (i = 0; i < z; ++i)
			for (j = 0; j < z; ++j)
				ztBc[i][j] = m;
		for (i = 0; i < z; ++i)
			ztBc[i][x[0]] = m - 1;
		for (i = 1; i < m - 1; ++i)
			ztBc[x[i - 1]][x[i]] = m - 1 - i;
	}

	private static void suffixes(char[] x, int[] suff) {
		int f = 0, g, i, m = x.length;

		suff[m - 1] = m;
		g = m - 1;
		for (i = m - 2; i >= 0; --i) {
			if (i > g && suff[i + m - 1 - f] < i - g)
				suff[i] = suff[i + m - 1 - f];
			else {
				if (i < g)
					g = i;
				f = i;
				while (g >= 0 && x[g] == x[g + m - 1 - f])
					--g;
				suff[i] = f - g;
			}
		}
	}

	private static void preBmGs(char[] x, int bmGs[]) {
		int i, j, m = x.length;
		int[] suff = new int[m];

		suffixes(x, suff);

		for (i = 0; i < m; ++i)
			bmGs[i] = m;
		j = 0;
		for (i = m - 1; i >= 0; --i)
			if (suff[i] == i + 1)
				for (; j < m - 1 - i; ++j)
					if (bmGs[j] == m)
						bmGs[j] = m - 1 - i;
		for (i = 0; i <= m - 2; ++i)
			bmGs[m - 1 - suff[i]] = m - 1 - i;
	}

	private static int calculateZtBcSize(char[] x, char[] y) {
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
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		if(calculateZtBcSize(x, y) > 256)
			return null;
		
		int[][] ztBc = new int[256][256];
		int[] bmGs = new int[m];

		/* Preprocessing */
		preZtBc(x, ztBc);
		preBmGs(x, bmGs);

		/* Searching */
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			while (i >= 0 && x[i] == y[i + j])
				--i;
			if (i < 0) {
				result.add(j);
				j += bmGs[0];
			} else if(j + m - 2 >= 0) {
				j += Math.max(bmGs[i], ztBc[y[j + m - 2]][y[j + m - 1]]);
			} else
				j += bmGs[i];
		}

		return result;
	}
	
	public static ZT compile(String pattern) {
		char[] x = pattern.toCharArray();
		int m = x.length;
		
		int[][] ztBc = new int[256][256];
		int[] bmGs = new int[m];

		preZtBc(x, ztBc);
		preBmGs(x, bmGs);
		
		ZT zt = new ZT();
		zt.bmGs = bmGs;
		zt.m = m;
		zt.x = x;
		zt.ztBc = ztBc;
		
		return zt;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		if(calculateZtBcSize(x, y) > 256)
			return null;
		
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			while (i >= 0 && x[i] == y[i + j])
				--i;
			if (i < 0) {
				result.add(j);
				j += bmGs[0];
			} else if(j + m - 2 >= 0) {
				j += Math.max(bmGs[i], ztBc[y[j + m - 2]][y[j + m - 1]]);
			} else
				j += bmGs[i];
		}

		return result;
	}
	
	char[] x;
	int m;
	int[][] ztBc;
	int[] bmGs;

}
