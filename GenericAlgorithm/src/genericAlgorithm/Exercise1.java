package genericAlgorithm;

public class Zad1 {

	static GoalFunction calc1 = new GoalFunction() {

		@Override
		public double func(double x1,double x2) {
			return Math.round((-1*( Math.pow(x1, 2) ) - Math.pow(x2, 2) +2.0)*100000d)/100000d; 
		}
	};
	
	GenericAlgorithm ga1;
	
	public static void main(String[] args) {
		Zad1 f1 = new Zad1();
		f1.ga1 = new GenericAlgorithm(-2,2,100000d,2,200,1000,0.02,0.6,50);
		try {
			f1.ga1.run(calc1);
		}catch(CloneNotSupportedException cloneEx) {
			cloneEx.printStackTrace();
		}
	}

}
