package com.javacodegeeks.stringsearch;

import java.util.ArrayList;
import java.util.List;

public class FDM {

	private static void buildSuffixAutomaton(char[] x, Graph aut) {
		int i, art, init, last, p, q, r, m = x.length;
		char c;

		init = Automata.getInitial(aut);
		art = Automata.newVertex(aut);
		Automata.setSuffixLink(aut, init, art);
		last = init;
		for (i = 0; i < m; ++i) {
			c = x[i];
			p = last;
			q = Automata.newVertex(aut);
			Automata.setLength(aut, q, Automata.getLength(aut, p) + 1);
			Automata.setPosition(aut, q, Automata.getPosition(aut, p) + 1);
			while (p != init
					&& Automata.getTarget(aut, p, c) == Automata.UNDEFINED) {
				Automata.setTarget(aut, p, c, q);
				Automata.setShift(aut, p, c, Automata.getPosition(aut, q)
						- Automata.getPosition(aut, p) - 1);
				p = Automata.getSuffixLink(aut, p);
			}
			if (Automata.getTarget(aut, p, c) == Automata.UNDEFINED) {
				Automata.setTarget(aut, init, c, q);
				Automata.setShift(aut, init, c, Automata.getPosition(aut, q)
						- Automata.getPosition(aut, init) - 1);
				Automata.setSuffixLink(aut, q, init);
			} else if (Automata.getLength(aut, p) + 1 == Automata.getLength(
					aut, Automata.getTarget(aut, p, c)))
				Automata.setSuffixLink(aut, q, Automata.getTarget(aut, p, c));
			else {
				r = Automata.newVertex(aut);
				Automata.copyVertex(aut, r, Automata.getTarget(aut, p, c));
				Automata.setLength(aut, r, Automata.getLength(aut, p) + 1);
				Automata.setSuffixLink(aut, Automata.getTarget(aut, p, c), r);
				Automata.setSuffixLink(aut, q, r);
				while (p != art
						&& Automata.getLength(aut, Automata
								.getTarget(aut, p, c)) >= Automata.getLength(
								aut, r)) {
					Automata.setShift(aut, p, c, Automata.getPosition(aut,
							Automata.getTarget(aut, p, c))
							- Automata.getPosition(aut, p) - 1);
					Automata.setTarget(aut, p, c, r);
					p = Automata.getSuffixLink(aut, p);
				}
			}
			last = q;
		}
		Automata.setTerminal(aut, last);
		while (last != init) {
			last = Automata.getSuffixLink(aut, last);
			Automata.setTerminal(aut, last);
		}
	}

	public static List<Integer> findAll(String pattern, String source) {
		char[] x = pattern.toCharArray(), y = source.toCharArray();
		int j, init, ell, state, m = x.length, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		Graph aut;

		/* Preprocessing */
		aut = Automata.newSuffixAutomaton(2 * (m + 2), 2 * (m + 2)
				* 65536);
		buildSuffixAutomaton(x, aut);
		init = Automata.getInitial(aut);

		/* Searching */
		ell = 0;
		state = init;
		for (j = 0; j < n; ++j) {
			if (Automata.getTarget(aut, state, y[j]) != Automata.UNDEFINED) {
				++ell;
				state = Automata.getTarget(aut, state, y[j]);
			} else {
				while (state != init
						&& Automata.getTarget(aut, state, y[j]) == Automata.UNDEFINED)
					state = Automata.getSuffixLink(aut, state);
				if (Automata.getTarget(aut, state, y[j]) != Automata.UNDEFINED) {
					ell = Automata.getLength(aut, state) + 1;
					state = Automata.getTarget(aut, state, y[j]);
				} else {
					ell = 0;
					state = init;
				}
			}
			if (ell == m)
				result.add(j - m + 1);
		}

		return result;
	}
	
	public static FDM compile(String pattern) {
		char[] x = pattern.toCharArray();
		int init, m = x.length;
		Graph aut;

		aut = Automata.newSuffixAutomaton(2 * (m + 2), 2 * (m + 2)
				* 65536);
		buildSuffixAutomaton(x, aut);
		init = Automata.getInitial(aut);
		
		FDM fdm = new FDM();
		fdm.aut = aut;
		fdm.m = m;
		fdm.init = init;
		
		return fdm;
	}
	
	public List<Integer> findAll(String source) {
		char[] y = source.toCharArray();
		int j, ell, state, n = y.length;
		List<Integer> result = new ArrayList<Integer>();
		
		ell = 0;
		state = init;
		for (j = 0; j < n; ++j) {
			if (Automata.getTarget(aut, state, y[j]) != Automata.UNDEFINED) {
				++ell;
				state = Automata.getTarget(aut, state, y[j]);
			} else {
				while (state != init
						&& Automata.getTarget(aut, state, y[j]) == Automata.UNDEFINED)
					state = Automata.getSuffixLink(aut, state);
				if (Automata.getTarget(aut, state, y[j]) != Automata.UNDEFINED) {
					ell = Automata.getLength(aut, state) + 1;
					state = Automata.getTarget(aut, state, y[j]);
				} else {
					ell = 0;
					state = init;
				}
			}
			if (ell == m)
				result.add(j - m + 1);
		}

		return result;
	}

	private Graph aut;
	private int m, init;
	
	private static class Graph {
		private int vertexNumber, edgeNumber, vertexCounter, initial;
		private int[] terminal, target, suffixLink, length, position, shift;
	}

	private static class Automata {

		private static final int UNDEFINED = -1;

		/*
		 * returns a new data structure for a graph with v vertices and e edges
		 */
		public static Graph newGraph(int v, int e) {
			Graph g = new Graph();

			g.vertexNumber = v;
			g.edgeNumber = e;
			g.initial = 0;
			g.vertexCounter = 1;
			return (g);
		}

		/*
		 * returns a new data structure for a automaton with v vertices and e
		 * edges
		 */
		public static Graph newAutomaton(int v, int e) {
			Graph aut;

			aut = newGraph(v, e);
			aut.target = new int[e];
			;
			aut.terminal = new int[v];
			return (aut);
		}

		/*
		 * returns a new data structure for a suffix automaton with v vertices
		 * and e edges
		 */
		public static Graph newSuffixAutomaton(int v, int e) {
			Graph aut;

			aut = newAutomaton(v, e);
			aut.target = new int[e];
			for (int i = 0; i < e; i++)
				aut.target[i] = UNDEFINED;
			aut.suffixLink = new int[v];
			aut.length = new int[v];
			aut.position = new int[v];
			aut.shift = new int[e];
			return (aut);
		}

		/*
		 * returns a new data structure for a trie with v vertices and e edges
		 */
		public static Graph newTrie(int v, int e) {
			Graph aut;

			aut = newAutomaton(v, e);
			aut.target = new int[e];
			for (int i = 0; i < e; i++)
				aut.target[i] = UNDEFINED;
			aut.suffixLink = new int[v];
			aut.length = new int[v];
			aut.position = new int[v];
			aut.shift = new int[e];
			return (aut);
		}

		/* returns a new vertex for graph g */
		public static int newVertex(Graph g) {
			int res = -1;
			if (g != null && g.vertexCounter <= g.vertexNumber)
				res = (g.vertexCounter++);
			return res;
		}

		/* returns the initial vertex of graph g */
		public static int getInitial(Graph g) {
			return g.initial;
		}

		/* returns true if vertex v is terminal in graph g */
		public static boolean isTerminal(Graph g, int v) {
			boolean res = false;
			if (g != null && g.terminal != null && v < g.vertexNumber)
				res = (g.terminal[v] == 1 ? true : false);
			return res;
		}

		/* set vertex v to be terminal in graph g */
		public static void setTerminal(Graph g, int v) {
			if (g != null && g.terminal != null && v < g.vertexNumber)
				g.terminal[v] = 1;
		}

		/*
		 * returns the target of edge from vertex v labelled by character c in
		 * graph g
		 */
		public static int getTarget(Graph g, int v, int c) {
			int res = -1;
			if (g != null && g.target != null && v < g.vertexNumber
					&& v * c < g.edgeNumber)
				res = (g.target[v * (g.edgeNumber / g.vertexNumber) + c]);
			return res;
		}

		/*
		 * add the edge from vertex v to vertex t labelled by character c in
		 * graph g
		 */
		public static void setTarget(Graph g, int v, int c, int t) {
			if (g != null && g.target != null && v < g.vertexNumber
					&& v * c <= g.edgeNumber && t < g.vertexNumber)
				g.target[v * (g.edgeNumber / g.vertexNumber) + c] = t;
		}

		/* returns the suffix link of vertex v in graph g */
		public static int getSuffixLink(Graph g, int v) {
			int res = -1;
			if (g != null && g.suffixLink != null && v < g.vertexNumber)
				res = (g.suffixLink[v]);
			return res;
		}

		/*
		 * set the suffix link of vertex v to vertex s in graph g
		 */
		public static void setSuffixLink(Graph g, int v, int s) {
			if (g != null && g.suffixLink != null && v < g.vertexNumber
					&& s < g.vertexNumber)
				g.suffixLink[v] = s;
		}

		/* returns the length of vertex v in graph g */
		public static int getLength(Graph g, int v) {
			int res = -1;
			if (g != null && g.length != null && v < g.vertexNumber)
				res = (g.length[v]);
			return res;
		}

		/* set the length of vertex v to integer ell in graph g */
		public static void setLength(Graph g, int v, int ell) {
			if (g != null && g.length != null && v < g.vertexNumber)
				g.length[v] = ell;
		}

		/* returns the position of vertex v in graph g */
		public static int getPosition(Graph g, int v) {
			int res = -1;
			if (g != null && g.position != null && v < g.vertexNumber)
				res = (g.position[v]);
			return res;
		}

		/* set the length of vertex v to integer ell in graph g */
		public static void setPosition(Graph g, int v, int p) {
			if (g != null && g.position != null && v < g.vertexNumber)
				g.position[v] = p;
		}

		/*
		 * returns the shift of the edge from vertex v labelled by character c
		 * in graph g
		 */
		public static int getShift(Graph g, int v, int c) {
			int res = -1;
			if (g != null && g.shift != null && v < g.vertexNumber
					&& v * c < g.edgeNumber)
				res = (g.shift[v * (g.edgeNumber / g.vertexNumber) + c]);
			return res;
		}

		/*
		 * set the shift of the edge from vertex v labelled by character c to
		 * integer s in graph g
		 */
		public static void setShift(Graph g, int v, int c, int s) {
			if (g != null && g.shift != null && v < g.vertexNumber
					&& v * c <= g.edgeNumber)
				g.shift[v * (g.edgeNumber / g.vertexNumber) + c] = s;
		}

		/*
		 * copies all the characteristics of vertex source to vertex target in
		 * graph g
		 */
		public static void copyVertex(Graph g, int target, int source) {
			if (g != null && target < g.vertexNumber && source < g.vertexNumber) {
				if (g.target != null)
					for (int i = 0; i < (g.edgeNumber / g.vertexNumber); i++)
						g.target[target * (g.edgeNumber / g.vertexNumber) + i] = g.target[source
								* (g.edgeNumber / g.vertexNumber) + i];
				if (g.shift != null)
					for (int i = 0; i < (g.edgeNumber / g.vertexNumber); i++)
						g.shift[target * (g.edgeNumber / g.vertexNumber) + i] = g.shift[source
								* (g.edgeNumber / g.vertexNumber) + i];
				if (g.terminal != null)
					g.terminal[target] = g.terminal[source];
				if (g.suffixLink != null)
					g.suffixLink[target] = g.suffixLink[source];
				if (g.length != null)
					g.length[target] = g.length[source];
				if (g.position != null)
					g.position[target] = g.position[source];
			}
		}

	}

}
