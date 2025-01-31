package genericAlgorithm;

import java.util.ArrayList;
import java.util.Arrays;
public class GenericAlgorithm {
	
	public double Ai;
	public double Bi;
	private double mutationPropability;
	private double crossingPropability;
	private double env;
	
	private int n, populationLength;
	private double precision;
	private int times;
	
	private static boolean firstRun = true;
	
	public GenericAlgorithm(double _Ai, double _Bi,double _precision,int _n,int _populationLength,double _env,double _mutationPropability, double _crossingPropability, int _times) {
		Ai = _Ai;
		Bi = _Bi;
		mutationPropability = _mutationPropability;
		crossingPropability = _crossingPropability;
		precision = _precision;
		populationLength = _populationLength;
		env = _env;
		n = _n;
		times = _times;
		
	}
	
	public void run(GoalFunction goal) throws CloneNotSupportedException{
		
		//1 krok inicjalizacja poczatkowej populacji chromosomow
		//2 krok ocena przystosowania chromosomow w populacji
		//3 krok: selekcja choromosomow: ruletka
		//4 krok: zastpspwanie operatorow genetycznych
		//	mutowanie
		//	krzyzowanie
		//5 krok: stworzenie nowej popualcji
		
		FileSaver globalMaxes = new FileSaver(String.format("globalMAX%d .txt",populationLength));
		double AVGMAX [][] = new double[times][(int)(1000)];
		double AVGS[][] = new double[times][(int)(1000)];
		double AVG_GLOBALMAX[][] = new double[50][1000];
		
		for(int i = 0 ; i < times; i++) {
			int generation  = 1;
			Chromosome baseChromosome = new Chromosome(Ai,Bi,n,precision);
			//krok 1 & 2
			Population population = new Population(baseChromosome,populationLength);
			population.adaptPopulation(goal);	
			int j = 0;
			
			int adaptationNR = 0;
			
			while(adaptationNR < env) {
				//krok 3
				
				if(firstRun == true && adaptationNR % 20 == 0) {
					FileSaver adaptationSteps = new FileSaver(String.format("adaptationSteps%d .txt", populationLength));
					adaptationSteps.WriteToFile(String.format("%d", adaptationNR));
				}
				
				population = new DecisionRoulette(population,"MAX").newPopulation;
				AVGMAX[i][j] = population.Adaptation.MAX;
				AVGS[i][j] = population.Adaptation.AVG;
				//krok 4 & 5
				population = crossingChromosomes(population);
				population = mutatePopulation(population);
				
				System.out.println("GENERACJA "+generation);
				generation++;
				population.Adaptation.showAdaptation();
				j++;
				adaptationNR++;	
			}
			firstRun = false;
			globalMaxes.WriteToFile(String.format("%g",population.GLOBALMAX));
		}
		
		FileSaver localMax = new FileSaver(String.format("localMax%d .txt",populationLength));
		FileSaver localAVG = new FileSaver(String.format("localAVG%d .txt",populationLength));
		double localMaxes[] =  calculateAVGLocal(AVGMAX);
		double localAVGS[] = calculateAVGLocal(AVGS);
		double globalMaxesArr[] = calculateAVGLocal(AVG_GLOBALMAX);
		
		for(int i = 0; i < localMaxes.length;i++) {
			localMax.WriteToFile(String.format("%g", localMaxes[i]));
			localAVG.WriteToFile(String.format("%g", localAVGS[i]));
			globalMaxes.WriteToFile(String.format("%g",globalMaxesArr[i]));
		}
	}
	
	
	private Population mutatePopulation(Population population) {
		
		Mutation mutation = new Mutation(mutationPropability);
		int length = population.population.length;
		for(int i = 0; i < length;i++) {
			
			if(Math.random() < mutationPropability) {
				population.population[i] = mutation.genomeMutation(population.population[i]);
			}
		}
		
		return population;
	}
	
	private Population crossingChromosomes(Population crossingPopulation) throws CloneNotSupportedException {
		
		int randomChromosome;
		int chromosomeLength = crossingPopulation.population[0].getChromosomeLength();
		int length = crossingPopulation.population.length;
		double randomPropability = 0.0;
		
		Chromosome parentX,parentY;
		for(int i = 0;i<length;i++) {
			randomPropability = Math.random();
			if(randomPropability<=crossingPropability) {
				randomChromosome = (int)(Math.random()*length);
				Intersection2P intersection2P = new Intersection2P(chromosomeLength);
				parentX = (Chromosome) crossingPopulation.population[i].clone();
				parentY = (Chromosome) crossingPopulation.population[randomChromosome].clone();
				crossingPopulation.population[i] = intersection2P.getChild(parentX, parentY);
				crossingPopulation.population[randomChromosome] = intersection2P.getChild(parentY, parentX);
			}
		}
		
		return crossingPopulation;
	}
	
	
	private double[] calculateAVGLocal(double[][]arr) {
		double AVGLocalMAX [] = new double[times];
		for(int i = 0; i < times;i++) {
			AVGLocalMAX[i] = calculateAVG(arr[i]);
		}
		return AVGLocalMAX;
	}
	
	private double calculateAVG(double[] arr) {
		return Arrays.stream(arr).average().getAsDouble();
	}
	
	
}
