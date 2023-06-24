package it.polito.tdp.imdb.model;

import java.util.*;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private List<Director> allDirector;
	private List<Movie> allMovie;
	private Map<Integer,Director> directorMap;
	private Map<Integer,Movie> movieMap;
	private Graph<Director,DefaultWeightedEdge> grafo;
	private List<Director> vertici;
	private List<Coppia> archi;
	
	private List<Director> migliore;
	
	public Model() {
		this.dao=new ImdbDAO();
		this.allDirector=dao.listAllDirectors();
		this.directorMap=new HashMap<>();
		for(Director d : allDirector) {
			this.directorMap.put(d.getId(), d);
		}
		this.allMovie=dao.listAllMovies();
		this.movieMap=new HashMap<>();
		for(Movie m : allMovie) {
			this.movieMap.put(m.getId(), m);
		}
	}
	
	public List<Director> calcolaGruppo(Director d, int c){
		this.migliore=new ArrayList<>();
		List<Director> parziale = new ArrayList<>();
		parziale.add(d);
		this.migliore=new ArrayList<>(parziale);
		ricorsione(parziale, c, this.raggiungibili(d));
		return migliore;
	}
	
	private void ricorsione(List<Director> parziale, int c, List<Director> raggiungibili) {
		if(parziale.size()>migliore.size()) {
			migliore = new ArrayList<>(parziale);
		}
		for(Director d : raggiungibili) {
			DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(parziale.size()-1), d);
//			if(e!=null) {
				if(!parziale.contains(d) && (this.calcolaParziale(parziale)+this.grafo.getEdgeWeight(e))<=c) {
					parziale.add(d);
					ricorsione(parziale,c,this.raggiungibili(d));
					parziale.remove(d);
				}
//			}
		}
	}

	private int calcolaParziale(List<Director> parziale) {
		int r = 0;
		for(int i=1;i<parziale.size();i++) {
			DefaultWeightedEdge e = this.grafo.getEdge(parziale.get(i), parziale.get(i-1));
			r += (int) this.grafo.getEdgeWeight(e);
		}
		return r;
	}

	public void creaGrafo(Integer anno) {
		this.grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		this.vertici=new ArrayList<>();
		this.vertici=dao.getVertici(anno,directorMap);
		Graphs.addAllVertices(this.grafo, this.vertici);
//		System.out.println("Ci sono "+this.grafo.vertexSet().size()+" vertici.");

		this.archi=new ArrayList<>();
		this.archi=dao.getArchi(anno, directorMap);
		for(Coppia c : archi) {
			Graphs.addEdge(this.grafo, c.getD1(), c.getD2(), c.getN());
		}
//		System.out.println("Ci sono "+this.grafo.edgeSet().size()+" archi.");
	}
	
	public List<DirettoreGrado> getAdiacenti(Director d){
		List<DirettoreGrado> s = new ArrayList<>();
		List<Director> adiacenti = Graphs.successorListOf(this.grafo, d);
		for(Director d1 : adiacenti) {
			for(Coppia c : this.archi) {
				if((c.getD1().equals(d) && c.getD2().equals(d1)) || (c.getD1().equals(d1) && c.getD2().equals(d))) {
					s.add(new DirettoreGrado(d1,c.getN()));
				}
			}
		}
		s.sort(null);
		return s;
	}
	
	public int getArchiSize() {
		return this.grafo.edgeSet().size();
	}

	public int getVerticiSize() {
		return this.grafo.vertexSet().size();
	}
	
	public List<Director> getVertici() {
		List<Director> s = new ArrayList<>();
		for(Director d : this.grafo.vertexSet()) {
			s.add(d);
		}
		s.sort(null);
		return s;
	}
	
//	private List<Director> raggiungibili(Director partenza){
//		DepthFirstIterator<Director,DefaultWeightedEdge> visita = new DepthFirstIterator<>(grafo,partenza);
//		List<Director> raggiungibili = new ArrayList<>();
//		while(visita.hasNext()) {
//			Director d = visita.next();
//			raggiungibili.add(d);
//		}
//		return raggiungibili;
//	}
	
	public List<Director> raggiungibili(Director partenza){
		return Graphs.successorListOf(this.grafo, partenza);
	}
}
