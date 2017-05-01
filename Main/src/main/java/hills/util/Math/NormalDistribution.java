package hills.util.Math;

/**
 * Created by Anders on 2017-03-20.
 */
public class NormalDistribution {

	/**
	 * Calculates the value y for the normal distribution function
	 * 
	 * @param x
	 *            the value to be calculated in f(x) = y
	 * @param maen
	 *            the maen value in the normal dist. function were f(x) reaches
	 *            it's peak value
	 * @param sDeviation
	 *            the standard deviation for the normal distribution function
	 * @return the value y in the equation f(x)=y
	 */
	private static double equation(double x, double maen, double sDeviation) {
		double d = Math.sqrt(2 * sDeviation * sDeviation * Math.PI);
		if (d == 0.0) {
			return 0;
		} else {
			return Math.exp(-((x - maen) * (x - maen)) / d);
		}
	}

	/**
	 * Calculates the value y for the normal distribution function with the maen
	 * value to 0
	 * 
	 * @param x
	 *            the value to be calculated in f(x) = y
	 * @param sDeviation
	 *            the standard deviation for the normal distribution function
	 * @return the value y in the equation f(x)=y
	 */
	public static double solve(double x, double sDeviation) {
		return equation(x, 0, sDeviation);
	}

	/**
	 * Calculates the value y for the normal distribution function
	 * 
	 * @param x
	 *            the value to be calculated in f(x) = y
	 * @param maen
	 *            the maen value in the normal dist. function were f(x) reaches
	 *            it's peak value
	 * @param sDeviation
	 *            the standard deviation for the normal distribution function
	 * @return the value y in the equation f(x)=y
	 */
	public static double solve(double x, double maen, double sDeviation) {
		return equation(x, maen, sDeviation);
	}

	/**
	 * Calculates the value y for the normal distribution function and adapt the
	 * value to fit the interval between the min and max value
	 * 
	 * @param x
	 *            the value to be calculated in f(x) = y
	 * @param maen
	 *            the maen value in the normal dist. function were f(x) reaches
	 *            it's peak value
	 * @param sDeviation
	 *            the standard deviation for the normal distribution function
	 * @param min
	 *            the minimum value of the function
	 * @param max
	 *            the maximum value of the function
	 * @return the value y in the equation f(x)=y
	 */
	public static double solve(double x, double maen, double sDeviation,
			double min, double max) {
		return interval(x, maen, sDeviation, max - min) + min;
	}

	/**
	 * Calculates the value y for the normal distribution function and adapts it
	 * to fit in the interval between 0 and diff
	 * 
	 * @param x
	 *            the value to be calculated in f(x) = y
	 * @param maen
	 *            the maen value in the normal dist. function were f(x) reaches
	 *            it's peak value
	 * @param sDeviation
	 *            the standard deviation for the normal distribution function
	 * @param diff
	 *            fits the y-value to the value of diff
	 * @return the value y in the equation f(x)=y
	 */
	private static double interval(double x, double maen, double sDeviation,
			double diff) {
		double d = equation(x, maen, sDeviation);
		double multiple = diff / equation(maen, maen, sDeviation);
		return d * multiple;
	}

}
