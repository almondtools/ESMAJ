package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class GS {

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

	private static void search(char[] x, int p, int n, int m, int s, int q,
			char[] y, int p1, int q1, int k, List<Integer> result) {
		while (p <= n - m) {
			while (p + s + q < n && x[s + q] == y[p + s + q])
				++q;
			if (q == m - s && arrayCmp(x, 0, y, p, (s + 1)) == 0) {
				result.add(p);
			}
			if (q == p1 + q1) {
				p += p1;
				q -= p1;
			} else {
				p += (q / k + 1);
				q = 0;
			}
		}
	}

	private static void parse(char[] x, char[] y, int s, int q2, int p2,
			int q1, int p1, int q, int p, int k, int m, int n, List<Integer> result) {
		
		while (true) {
			while (x[s + q1] == x[s + p1 + q1])
				++q1;
			while (p1 + q1 >= k * p1) {
				s += p1;
				q1 -= p1;
			}
			p1 += (q1 / k + 1);
			q1 = 0;
			if (p1 >= p2)
				break;
		}
		newP1(x, y, s, q1, p1, q2, p2, q, p, k, m, n, result);
	}

	private static void newP2(char[] x, char[] y, int s, int q2, int p2, int q1,
			int p1, int p, int q, int k, int m, int n, List<Integer> result) {
		while (x[s + q2] == x[s + p2 + q2] && p2 + q2 < k * p2)
			++q2;
		if (p2 + q2 == k * p2)
			parse(x, y, s, q2, p2, q1, p1, q, p, k, m, n, result);
		else if (s + p2 + q2 == m)
			search(x, p, n, m, s, q, y, p1, q1, k, result);
		else {
			if (q2 == p1 + q1) {
				p2 += p1;
				q2 -= p1;
			} else {
				p2 += (q2 / k + 1);
				q2 = 0;
			}
			newP2(x, y, s, q2, p2, q1, p1, p, q, k, m, n, result);
		}
	}

	private static void newP1(char[] x, char[] y, int s, int q1, int p1, int q2,
			int p2, int q, int p, int k, int m, int n, List<Integer> result) {
		while (x[s + q1] == x[s + p1 + q1])
			++q1;
		if (p1 + q1 >= k * p1) {
			p2 = q1;
			q2 = 0;
			newP2(x, y, s, q2, p2, q1, p1, p, q, k, m, n, result);
		} else {
			if (s + p1 + q1 == m)
				search(x, p, n, m, s, q, y, p1, q1, k, result);
			else {
				p1 += (q1 / k + 1);
				q1 = 0;
				newP1(x, y, s, q1, p1, q2, p2, q, p, k, m, n, result);
			}
		}
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] ptrn = pattern.toCharArray(), y = source.toCharArray();
		char[] x = new char[ptrn.length + 1];
		System.arraycopy(ptrn, 0, x, 0, ptrn.length);
		int k = 4, p1 = 1, p = 0, q = 0, s = 0, q1 = 0, p2 = 0, q2 = 0, m = ptrn.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		newP1(x, y, s, q1, p1, q2, p2, q, p, k, m, n, result);
		
		return result;
	}

}
