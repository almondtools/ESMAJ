package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class SMN {

	private static int getTransition(char[] x, int p, Cell[] lst, char c) {
		int m = x.length;
		Cell cell;

		if (p < m - 1 && x[p + 1] == c)
			return (p + 1);
		else if (p > -1) {
			cell = lst[p];
			while (cell != null)
				if (x[cell.element] == c)
					return (cell.element);
				else
					cell = cell.next;
			return (-1);
		} else
			return (-1);
	}

	private static void setTransition(int p, int q, Cell[] list) {
		Cell cell = new Cell();

		cell.element = q;
		cell.next = list[p];
		list[p] = cell;
	}

	private static int preSimon(char[] x, Cell[] list) {
		int i, k, ell, m = x.length;
		Cell cell;

		for (i = 0; i < (m - 2); i++)
			list[i] = null;
		ell = -1;
		for (i = 1; i < m; ++i) {
			k = ell;
			cell = (ell == -1 ? null : list[k]);
			ell = -1;
			if (x[i] == x[k + 1])
				ell = k + 1;
			else
				setTransition(i - 1, k + 1, list);
			while (cell != null) {
				k = cell.element;
				if (x[i] == x[k])
					ell = k;
				else
					setTransition(i - 1, k, list);
				cell = cell.next;
			}
		}
		return (ell);
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int j, ell, state, m = x.length, n = y.length;
		Cell[] list = new Cell[m];
		List<Integer> result = new ArrayList<Integer>();

		/* Preprocessing */
		ell = preSimon(x, list);

		/* Searching */
		for (state = -1, j = 0; j < n; ++j) {
			state = getTransition(x, state, list, y[j]);
			if (state >= m - 1) {
				result.add(j - m + 1);
				state = ell;
			}
		}
		
		return result;
	}
	
	public static SMN compile(String pattern) {
		char[] x = pattern.toCharArray();
		int ell, m = x.length;
		Cell[] list = new Cell[m];

		ell = preSimon(x, list);
		
		SMN smn = new SMN();
		smn.ell = ell;
		smn.list = list;
		smn.m = m;
		smn.x = x;
		
		return smn;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, state, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		for (state = -1, j = 0; j < n; ++j) {
			state = getTransition(x, state, list, y[j]);
			if (state >= m - 1) {
				result.add(j - m + 1);
				state = ell;
			}
		}
		
		return result;
	}
	
	private char[] x;
	private int ell, m;
	private Cell[] list;

	private static class Cell {
		int element;
		Cell next;
	}

}
