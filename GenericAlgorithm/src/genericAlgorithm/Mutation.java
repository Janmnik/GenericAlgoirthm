package genericAlgorithm;

public class Mutation {
	private double propability = 0.1;
	Chromosome genome;
	
	public Mutation(Chromosome genome,double propability) {
		this.propability = propability; //prawdopodobienstwo
		this.genome = genome;
	}
	
	public Mutation(double propability) {
		this.propability = propability; //prawdopodobienstwo
	}
	
	public Chromosome genomeMutation() {
		for(int i = 0; i < genome.chromosome.length;i++) {
			double _propability = Math.random();
			if(_propability < propability) {
				genome.chromosome[i] = genome.chromosome[i] == '0' ? '1' : '0';
			}
		}
		return genome;
	}
	
	public Chromosome genomeMutation(Chromosome genome) {
		for(int i = 0; i < genome.chromosome.length;i++) {
			double _propability = Math.random();
			if(_propability < propability) {
				genome.chromosome[i] = genome.chromosome[i] == '0' ? '1' : '0';
			}
		}
		return genome;
	}
	
}
