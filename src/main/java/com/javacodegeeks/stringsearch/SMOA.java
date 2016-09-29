package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class SMOA {

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

	/* Compute the next maximal suffix. */
	private static void nextMaximalSuffix(char[] y, int yIdx, int m, int[] i,
			int[] j, int[] k, int[] p) {
		char a, b;
		int c = y.length;

		while (j[0] + k[0] < m && yIdx + i[0] + k[0] < c && yIdx + j[0] + k[0] < c) {
			a = y[yIdx + i[0] + k[0]];
			b = y[yIdx + j[0] + k[0]];
			if (a == b)
				if (k[0] == p[0]) {
					(j[0]) += p[0];
					k[0] = 1;
				} else
					++(k[0]);
			else if (a > b) {
				(j[0]) += k[0];
				k[0] = 1;
				p[0] = j[0] - i[0];
			} else {
				i[0] = j[0];
				++(j[0]);
				k[0] = p[0] = 1;
			}
		}
	}

	/* String matching on ordered alphabets algorithm. */
	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, m = x.length, n = y.length;
		int[] ip = new int[1], jp = new int[1], k = new int[1], p = new int[1];
		List<Integer> result = new ArrayList<Integer>();

		/* Searching */
		ip[0] = -1;
		i = j = jp[0] = 0;
		k[0] = p[0] = 1;
		while (j <= n - m) {
			while (i + j < n && i < m && x[i] == y[i + j])
				++i;
			if (i == 0) {
				++j;
				ip[0] = -1;
				jp[0] = 0;
				k[0] = p[0] = 1;
			} else {
				if (i >= m)
					result.add(j);
				nextMaximalSuffix(y, j, i + 1, ip, jp, k, p);
				if (ip[0] < 0
						|| (ip[0] < p[0] && arrayCmp(y, j, y, j + p[0],
								ip[0] + 1) == 0)) {
					j += p[0];
					i -= p[0];
					if (i < 0)
						i = 0;
					if (jp[0] - ip[0] > p[0])
						jp[0] -= p[0];
					else {
						ip[0] = -1;
						jp[0] = 0;
						k[0] = p[0] = 1;
					}
				} else {
					j += (Math.max(ip[0] + 1, Math
							.min(i - ip[0] - 1, jp[0] + 1)) + 1);
					i = jp[0] = 0;
					ip[0] = -1;
					k[0] = p[0] = 1;
				}
			}
		}

		return result;
	}

}
