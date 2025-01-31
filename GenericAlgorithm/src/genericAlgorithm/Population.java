package genericAlgorithm;

import java.util.Arrays;

public class Population {
	//klasa wewnetrzna odpowiedzialna za wylicznie przystosowania populacji
	class Adaptation{
		
		Chromosome population [];
		double adaptation [];
		GoalFunction goal;
		double AVG,MIN,MAX;
		
		Adaptation(GoalFunction goal, Chromosome[] population){
			this.goal = goal;
			this.population = population;
			this.adaptation = adaptPopulation(goal);
			this.AVG = calcAVGAdaptation();
			this.MIN = getMin(adaptation);
			this.MAX = getMax(adaptation);
		}
		
		public double adaptGenome(Chromosome genome) {
			return goal.func(genome.decoding(1),genome.decoding(2));
		}
			
		private double [] adaptPopulation(GoalFunction goal) {
			
			int n = population.length;
			double adaptation [] = new double[n];
			
			for(int i = 0; i<n;i++) {
				adaptation[i] = goal.func(population[i].decoding(1),population[i].decoding(2));
			}
			
			this.MIN = getMin(adaptation);
			this.MAX = getMax(adaptation);
			
			if(GLOBALMAX < MAX)
				GLOBALMAX = MAX;
			
			return adaptation;
		}
		
		private double getMax(double adaptation[]) {
			return Arrays.stream(adaptation).max().getAsDouble();
		}
		
		private double getMin(double adaptation[]) {
			return Arrays.stream(adaptation).min().getAsDouble();
		}
		
		public void setAdaptation(double _adaptation[]) 
		{
			this.adaptation = _adaptation;
			calcAVGAdaptation();
			//this.MIN = getMin(adaptation);
		}
		
		
		public void showAdaptation() {
			int n = adaptation.length;
			for(int i = 0 ; i < n; i++) {
				System.out.println("ADAP:"+adaptation[i]+"| X1:"+population[i].decoding(1)+"; X2:"+population[i].decoding(2));
			}
			System.out.println("MIN:"+getMin(adaptation)+" MAX:"+getMax(adaptation));
		}
		
		public int invidualsBelowAVG() {
			int count = 0;
			for(int i = 0 ; i < n; i++) {
				if(adaptation[i] < AVG)
					count++;
			}
			return count;
		}
		
		public int invidualsAboveOrEqualAVG() {
			int count = 0;
			int n = adaptation.length;
			for(int i = 0 ; i < n; i++) {
				if(adaptation[i] >= AVG)
					count++;
			}
			return count;
		}
		
		private double calcAVGAdaptation() {
			int n = adaptation.length;
			double sum = 0.0;
			for(int i = 0 ; i < n; i++) {
				sum+=adaptation[i];
			}
			return sum / n ;
		}
	}
	
	int n = 1;
	
	Adaptation Adaptation;
	Chromosome population[];
	Chromosome base;
	static double GLOBALMAX = 0.0;

	
	public Population(Chromosome base,int n) {
		this.base = base;
		this.n = n;
		population = generatePopulation();
	}
	
	public void adaptPopulation(GoalFunction goal) {
		this.Adaptation = new Adaptation(goal,population);
	}
	
	private Chromosome[] generatePopulation() {
		Chromosome[] population = new Chromosome[n];
		
		for(int i = 0; i < n; i++) {
			population[i] = new Chromosome(base.getStart(),base.getEnd(),base.n,base.precision);
		}
		return population;
	}
	
	public void showPopulationAsBinary() {
		for(int i = 0 ; i < n; i++) {
			System.out.println(population[i].chromosome);
		}
	}
	
	public void showPopulation() {
		for(int i = 0 ; i < n; i++) {
			System.out.println("CHROMOSOME"+(i+1));
			System.out.println(new String(population[i].chromosome)+"|"+Adaptation.adaptation[i]);
			for(int j = 1 ; j <= population[i].n; j++)
			{
				System.out.print(population[i].decoding(j)+",");
			}
			System.out.println();
		}
	}
	
	public boolean isBelowAVG(Chromosome genome) {
		return Adaptation.adaptGenome(genome) < Adaptation.AVG;
	}
	
	public boolean isAboveAVG(Chromosome genome) {
		return Adaptation.adaptGenome(genome) >= Adaptation.AVG;
	}
	
	public Population setPopulation(Chromosome[] _population) {
		Population population = new Population(this.base,this.n);
		population.population = _population;
		population.adaptPopulation(Adaptation.goal);
		population.Adaptation.AVG = population.Adaptation.calcAVGAdaptation();
		return population;
	}
	
	public void showDetails() {
//		System.out.println("========POPULATION AS BINARY==========");
//		showPopulationAsBinary();
//		System.out.println("========POPULATION'S ADAPTATION==========");
//		Adaptation.showAdaptation();
		System.out.println("========POPULATION'S VIEW==========");
		showPopulation();
		System.out.println("======== AVG POPULATION'S ADAPTATION==========");
		System.out.println(Adaptation.AVG);
		System.out.println("======== COUNT BELOW AVG==========");
		System.out.println(Adaptation.invidualsBelowAVG());
		System.out.println("======== COUNT ABOVE OR EQUAL AVG==========");
		System.out.println(Adaptation.invidualsAboveOrEqualAVG());
	}
	
	
}