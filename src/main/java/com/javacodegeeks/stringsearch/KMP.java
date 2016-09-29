package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class KMP {

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
		int i, j, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		int[] kmpNext = new int[x.length];

		/* Preprocessing */
		preKmp(x, kmpNext);

		/* Searching */
		i = j = 0;
		while (j < n) {
			while (i > -1 && x[i] != y[j])
				i = kmpNext[i];
			i++;
			j++;
			if (i >= m) {
				result.add(j - i);
				i = kmpNext[i];
			}
		}

		return result;
	}
	
	public static KMP compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int m = ptrn.length;

		int[] kmpNext = new int[x.length];

		preKmp(x, kmpNext);
		
		KMP kmp = new KMP();
		kmp.kmpNext = kmpNext;
		kmp.m = m;
		kmp.x = x;
		
		return kmp;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		i = j = 0;
		while (j < n) {
			while (i > -1 && x[i] != y[j])
				i = kmpNext[i];
			i++;
			j++;
			if (i >= m) {
				result.add(j - i);
				i = kmpNext[i];
			}
		}

		return result;
	}
	
	private char[] x;
	private int m;
	private int[] kmpNext;
	
}
