package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class AC {

	private static void preKmp(char[] x, int[] kmpNext) {
		int i, j, m = (x.length - 1);

		i = 0;
		j = kmpNext[0] = -1;
		while (i < m) {
			while (j > -1 && x[i] != x[j])
				j = kmpNext[j];
			i++;
			j++;
			if (x[i] == x[j])
				kmpNext[i] = kmpNext[j];
			else
				kmpNext[i] = j;
		}
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, j, k, ell, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] kmpNext = new int[x.length];

		/* Preprocessing */
		preKmp(x, kmpNext);
		for (ell = 1; x[ell - 1] == x[ell]; ell++)
			;
		if (ell == m)
			ell = 0;

		/* Searching */
		i = ell;
		j = k = 0;
		while (j <= n - m) {
			while (i < m && x[i] == y[i + j])
				++i;
			if (i >= m) {
				while (k < ell && x[k] == y[j + k])
					++k;
				if (k >= ell)
					result.add(j);
			}
			j += (i - kmpNext[i]);
			if (i == ell)
				k = Math.max(0, k - 1);
			else if (kmpNext[i] <= ell) {
				k = Math.max(0, kmpNext[i]);
				i = ell;
			} else {
				k = ell;
				i = kmpNext[i];
			}
		}

		return result;
	}

	public static AC compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int ell, m = ptrn.length;

		int[] kmpNext = new int[x.length];

		preKmp(x, kmpNext);
		for (ell = 1; x[ell - 1] == x[ell]; ell++)
			;
		if (ell == m)
			ell = 0;

		AC ac = new AC();
		ac.m = m;
		ac.ell = ell;
		ac.x = x;
		ac.kmpNext = kmpNext;

		return ac;
	}

	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, k, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		i = ell;
		j = k = 0;
		while (j <= n - m) {
			while (i < m && x[i] == y[i + j])
				++i;
			if (i >= m) {
				while (k < ell && x[k] == y[j + k])
					++k;
				if (k >= ell)
					result.add(j);
			}
			j += (i - kmpNext[i]);
			if (i == ell)
				k = Math.max(0, k - 1);
			else if (kmpNext[i] <= ell) {
				k = Math.max(0, kmpNext[i]);
				i = ell;
			} else {
				k = ell;
				i = kmpNext[i];
			}
		}

		return result;
	}

	private int m, ell;
	private char[] x;
	private int[] kmpNext;

}
