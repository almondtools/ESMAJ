package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class BOM {

	private static final char TRUE = 1;
	private static final char FALSE = 0;
	private static final int UNDEFINED = -1;

	private static int getTransition(char[] x, int p, Cell[] list, char c) {
		Cell cell;

		if (p > 0 && x[p - 1] == c)
			return (p - 1);
		else {
			cell = list[p];
			while (cell != null)
				if (x[cell.element] == c)
					return (cell.element);
				else
					cell = cell.next;
			return (UNDEFINED);
		}
	}

	private static void setTransition(int p, int q, Cell[] list) {
		Cell cell = new Cell();

		cell.element = q;
		cell.next = list[p];
		list[p] = cell;
	}

	private static void oracle(char[] x, char[] t, Cell[] list) {
		int i, p, q = UNDEFINED, m = x.length - 1;
		int[] s = new int[x.length];
		char c;

		s[m] = m + 1;
		for (i = m; i > 0; --i) {
			c = x[i - 1];
			p = s[i];
			while (p <= m && (q = getTransition(x, p, list, c)) == UNDEFINED) {
				setTransition(p, i - 1, list);
				p = s[p];
			}
			s[i - 1] = (p == m + 1 ? m : q);
		}
		p = 0;
		while (p <= m) {
			t[p] = TRUE;
			p = s[p];
		}
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, j, p, period = 0, q, shift, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();

		char[] t = new char[x.length];
		Cell[] list = new Cell[x.length];

		/* Preprocessing */
		for (i = 0; i < t.length; i++)
			t[i] = FALSE;
		oracle(x, t, list);

		/* Searching */
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			p = m;
			shift = m;
			while (i + j >= 0
					&& (q = getTransition(x, p, list, y[i + j])) != UNDEFINED) {
				p = q;
				if (t[p] == TRUE) {
					period = shift;
					shift = i;
				}
				--i;
			}
			if (i < 0) {
				result.add(j);
				shift = period;
			}
			j += shift;
		}

		return result;
	}
	
	public static BOM compile(String pattern) {
		char[] ptrn = pattern.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int i, m = ptrn.length;

		char[] t = new char[x.length];
		Cell[] list = new Cell[x.length];

		for (i = 0; i < t.length; i++)
			t[i] = FALSE;
		oracle(x, t, list);
		
		BOM bom = new BOM();
		bom.m = m;
		bom.x = x;
		bom.t = t;
		bom.list = list;
		
		return bom;
		
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int i, j, p, period = 0, q, shift, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		j = 0;
		while (j <= n - m) {
			i = m - 1;
			p = m;
			shift = m;
			while (i + j >= 0
					&& (q = getTransition(x, p, list, y[i + j])) != UNDEFINED) {
				p = q;
				if (t[p] == TRUE) {
					period = shift;
					shift = i;
				}
				--i;
			}
			if (i < 0) {
				result.add(j);
				shift = period;
			}
			j += shift;
		}

		return result;
	}
	
	private char[] x, t;
	private Cell[] list;
	private int m;

	private static class Cell {
		int element;
		Cell next;
	}

}
