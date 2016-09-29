package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class BNDM {

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int i, j, s, d, last, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		int[] b = new int[65536];

		/* Pre processing */
		for (i = 0; i < b.length; i++)
			b[i] = 0;
		s = 1;
		for (i = m - 1; i >= 0; i--) {
			b[x[i]] |= s;
			s <<= 1;
		}

		/* Searching phase */
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			last = m;
			d = ~0;
			while (i >= 0 && d != 0) {
				d &= b[y[j + i]];
				i--;
				if (d != 0) {
					if (i >= 0)
						last = i + 1;
					else
						result.add(j);
				}
				d <<= 1;
			}
			j += last;
		}

		
		return result;
	}
	
	public static BNDM compile(String pattern) {
		char[] x = pattern.toCharArray();
		int i, s, m = x.length;
		int[] b = new int[65536];

		for (i = 0; i < b.length; i++)
			b[i] = 0;
		s = 1;
		for (i = m - 1; i >= 0; i--) {
			b[x[i]] |= s;
			s <<= 1;
		}
		
		BNDM bndm = new BNDM();
		bndm.m = m;
		bndm.b = b;
		
		return bndm;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, d, last, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			last = m;
			d = ~0;
			while (i >= 0 && d != 0) {
				d &= b[y[j + i]];
				i--;
				if (d != 0) {
					if (i >= 0)
						last = i + 1;
					else
						result.add(j);
				}
				d <<= 1;
			}
			j += last;
		}

		
		return result;
	}
	
	private int[] b;
	private int m;
	
}
