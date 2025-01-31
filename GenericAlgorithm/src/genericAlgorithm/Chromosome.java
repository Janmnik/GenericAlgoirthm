package genericAlgorithm;

public class Chromosome implements Cloneable {
	private int start;
	private int end;
	
	private double Ai,Bi;
	double precision = 100000d;
	private int MI;
	int n=1;
	char [] chromosome;
	
	public Chromosome(double Ai, double Bi,int n, double precision) {
		this.Ai = Ai; // wartosc poczatku przedzialu wartosci
		this.Bi = Bi; // wartosc konica przedzialu wartosci
		this.n = n; // liczba genow
		this.precision = precision;
		MI = Mi();
		start = 0;
		
		end = n*MI;
		chromosome = generateChromosome();
		checkChromosome();
	}
	
	public void setChromosome(char [] values) {
		chromosome = values;
	}
	
	//wyliczenie ilu bit�w potrzebne jest do zakodowania 1 genu.
	public int Mi() {
		return (int)Math.round(Log2((Bi - Ai)*precision));
	}
	
	//c : generowanie losowo chromosomu w postaci binarnej
	private char [] generateChromosome() {
		char [] _chromosome = new char[end];
		for(int i = 0; i < end; i++) {
			_chromosome[i] = 0 + (int)(Math.random()*2) == 0 ? '0' : '1';
		}
		
		return _chromosome;
	}
			
	//dostanie dowolnego genu po przez podanie jego numeru
	public String getXn(int gen) {
		//System.out.println(gen);
		int avg_count = (int)(end - start)/n;
		char [] genN = new char[avg_count];
		int begin = (int)(gen-1)*avg_count;
		int j = 0; // inkrementuje dla genu
		
		if(end - begin < avg_count) {
			System.out.println("Wykraczam");
			avg_count = end-start; 
		}
		if(begin < 0) {
			begin *= (-1);
		}
		for(int i = begin; i<(begin+avg_count);i++) {
			genN[j] = chromosome[i];
			j++;
		}
		
		return new String(genN);
	}
	
	//dekodowanie zakodowanego binarnie genu na jego wartosc decymalna
	public double decoding(int gen) {
		double result = Ai + Integer.parseInt(getXn(gen),2) * (Bi-Ai) / (Math.pow(2,MI) - 1);
		return  Math.round(result*precision)/precision;
	}
	
	protected Object clone() throws CloneNotSupportedException{
		return super.clone();
		
	}
	public Chromosome makeCopy() {
		Chromosome copy= new Chromosome(this.Ai,this.Bi,this.n,this.precision);
		copy.chromosome = this.chromosome;
		return copy;
	}
	
	public int getChromosomeLength() {
		return chromosome.length;
	}
	
	public double getStart() {
		return Ai;
	}
	
	public double getEnd() {
		return Bi;
	}
	
	public String toString() {
		return new String(chromosome);
	}
	
	private void checkChromosome() {
		if(n > 1) {
			double x1 = decoding(1);
			double x2 = decoding(2);
			while( !((x1 >= -Ai && x1 <= Bi) || (x2 >= Ai && x2 <= Bi) )) {
				chromosome = generateChromosome();
				x1 = decoding(1);
				x2 = decoding(2);
			}
		}
		else {
		
			double x1 = decoding(1);
			while((x1 >= -Ai && x1 <= Bi)) {
				chromosome = generateChromosome();
				x1 = decoding(1);
			}
			
		}
	}
		
	private double Log2(double x) {
		return (Math.log(x) / Math.log(2));
	}
}