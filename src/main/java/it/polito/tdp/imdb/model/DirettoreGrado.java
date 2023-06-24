package it.polito.tdp.imdb.model;

public class DirettoreGrado implements Comparable<DirettoreGrado>{

	private Director d;
	private int n;
	
	public DirettoreGrado(Director d, int n) {
		super();
		this.d = d;
		this.n = n;
	}

	public Director getD() {
		return d;
	}

	public int getN() {
		return n;
	}

	@Override
	public int compareTo(DirettoreGrado o) {
		return -(this.n-o.n);
	}
	
}
