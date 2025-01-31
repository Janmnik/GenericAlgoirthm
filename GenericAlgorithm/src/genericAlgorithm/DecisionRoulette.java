package genericAlgorithm;

public class DecisionRoulette {

	Population basePopulation;
	Population newPopulation;
	double[] decisionRouletteSectors;
	private double u;
	double scaledAdaptation[];
	public DecisionRoulette(Population _population, String aim) {
		basePopulation = _population;
		
		scaledAdaptation = new double[_population.n];
		//u = przesuniecie; gdy wartosci dopasowania sa na "-" nale�y przesun�� warto�ci o min warto�� z populacji
		u = basePopulation.Adaptation.MIN;
		
		if(u <= 0) {
			_population.Adaptation.setAdaptation(scaleAdaptation(_population.Adaptation)); 
		}
		
		decisionRouletteSectors = calculateRouletteSectors(basePopulation);
		
		if (aim.toLowerCase().equals("max")) {
			newPopulation = selectMaxPopulationGenomes(basePopulation);
		} else {
			newPopulation = selectMinPopulationGenomes(basePopulation);
		}
	}
	
	
	private double [] scaleAdaptation(Population.Adaptation adaptation) {
		int length = adaptation.adaptation.length;
		double scaled [] = new double[length];
		for(int i = 0; i < length; i++) {
			scaled[i] = adaptation.adaptation[i]+Math.abs(u)+1;
		}
		return scaled;
	}
	
	private double sumAdaptation(Population.Adaptation adaptation) {
		double sum = 0;

		for (int i = 0; i < adaptation.adaptation.length; i++) {
			sum += adaptation.adaptation[i];
		}
		
		return sum;
	}

	double[] calculateRouletteSectors(Population population) {
		double begin = 0;
		double area = 0.0;
		double[] rouletteAreaSectors = new double[population.population.length];
		double sumAdaptation = sumAdaptation(population.Adaptation);
		
		int n = population.population.length;
		
		for (int i = 0; i < n; i++) {
			area = calculateArea(calculatePropability(population.Adaptation.adaptation[i], sumAdaptation));
			rouletteAreaSectors[i] = begin + area;
			begin += area;
		}
		return rouletteAreaSectors;
	}

	private double calculatePropability(double adaptation, double sumAdaptation) {
		return adaptation / sumAdaptation;
	}

	private double calculateArea(double propability) {
		return propability * 100;
	}

	private Chromosome getChromosomeFromRoulette(int percent) {
		int chromosomeNumber = 1;
		int lenght = decisionRouletteSectors.length;
		for (int i = 1; i < lenght; i++) {
			if (decisionRouletteSectors[i - 1] >= percent && decisionRouletteSectors[i] > percent) {
				break;
			}
			chromosomeNumber++;
		}
		return basePopulation.population[chromosomeNumber - 1];
	}

	public Population selectMaxPopulationGenomes(Population population) {

		int length = population.n;
		int[] randomDigits = new int[length];

		Chromosome[] population1 = new Chromosome[length];

		// losowanie liczb, ktore maja wskazywac obszar na kole, czyli wybor nowych
		// osobnikow do populacji
		for (int i = 0; i < length; i++) {
			randomDigits[i] = (int) (Math.random() * 100);
		}

		// tworzenie nowej populacji
		for (int i = 0; i < randomDigits.length; i++) {
			population1[i] = getChromosomeFromRoulette(randomDigits[i]);
		}

		Population newPopulation = basePopulation.setPopulation(population1);

		return newPopulation;
	}

	public Population selectMinPopulationGenomes(Population population) {
		return null;
	}

}