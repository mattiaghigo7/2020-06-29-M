package it.polito.tdp.imdb.model;

public class Coppia implements Comparable<Coppia>{

	private Director d1;
	private Director d2;
	private int n;
	
	public Coppia(Director d1, Director d2, int n) {
		super();
		this.d1 = d1;
		this.d2 = d2;
		this.n = n;
	}

	public Director getD1() {
		return d1;
	}

	public Director getD2() {
		return d2;
	}

	public int getN() {
		return n;
	}

	@Override
	public int compareTo(Coppia o) {
		return -(this.n-o.n);
	}
	
	
}
