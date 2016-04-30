package org.ssr.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GXUtil {
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static String getDateToString(Date date){
		if(date != null){
			return sdf.format(date);
		}
		return null;
	}
	
	public static String[] getLongArrayToStringArray(List<Long> longList){
		if(longList.isEmpty()){
			return null;
		}
		String[] strList = new String[longList.size()];
		int i = 0;
		for(Long longVal : longList){
			strList[i++] = String.valueOf(longVal);
		}
		return strList;
	}
	
	/**
     * Returns the sum number in the numbers list.
     *
     * @param numbers the numbers to calculate the sum.
     * @return the sum of the numbers.
     */
    public static double sum(List<Number> numbers) {
        double sum = 0;
        for (Number value : numbers) {
            sum += value.doubleValue();
        }
        return sum;
    }

    /**
     * Returns the mean number in the numbers list.
     *
     * @param numbers the numbers to calculate the mean.
     * @return the mean of the numbers.
     */
    public static double mean(List<Number> numbers) {
        double sum = sum(numbers);
        return sum / numbers.size();
    }

    /**
     * Returns the min number in the numbers list.
     *
     * @param numbers the numbers to calculate the min.
     * @return the min number in the numbers list.
     */
    public static double min(List<Number> numbers) {
        double min = Integer.MAX_VALUE;
        for (Number value : numbers) {
            double v = value.doubleValue();
            if (v < min) {
                min = v;
            }
        }
        return min;
    }

    /**
     * Returns the max number in the numbers list.
     *
     * @param numbers the numbers to calculate the max.
     * @return the max number in the numbers list.
     */
    public static double max(List<Number> numbers) {
        double max = Integer.MIN_VALUE;
        for (Number value : numbers) {
            double v = value.doubleValue();
            if (v > max) {
                max = v;
            }
        }
        return max;
    }

    /**
     * Returns the standard deviation of the numbers.
     * <p/>
     * Double.NaN is returned if the numbers list is empty.
     *
     * @param numbers       the numbers to calculate the standard deviation.
     * @param biasCorrected true if variance is calculated by dividing by n - 1. False if by n. stddev is a sqrt of the
     *                      variance.
     * @return the standard deviation
     */
    public static double stddev(List<Number> numbers, boolean biasCorrected) {
        double stddev = Double.NaN;
        int n = numbers.size();
        if (n > 0) {
            if (n > 1) {
                stddev = Math.sqrt(var(numbers, biasCorrected));
            }
            else {
                stddev = 0.0;
            }
        }
        return stddev;
    }

    /**
     * Computes the variance of the available values. By default, the unbiased "sample variance" definitional formula is
     * used: variance = sum((x_i - mean)^2) / (n - 1)
     * <p/>
     * The "population variance"  ( sum((x_i - mean)^2) / n ) can also be computed using this statistic.  The
     * <code>biasCorrected</code> property determines whether the "population" or "sample" value is returned by the
     * <code>evaluate</code> and <code>getResult</code> methods. To compute population variances, set this property to
     * <code>false</code>.
     *
     * @param numbers       the numbers to calculate the variance.
     * @param biasCorrected true if variance is calculated by dividing by n - 1. False if by n.
     * @return the variance of the numbers.
     */
    public static double var(List<Number> numbers, boolean biasCorrected) {
        int n = numbers.size();
        if (n == 0) {
            return Double.NaN;
        }
        else if (n == 1) {
            return 0d;
        }
        double mean = mean(numbers);
        List<Number> squares = new ArrayList<Number>();
        for (Number number : numbers) {
            double XminMean = number.doubleValue() - mean;
            squares.add(Math.pow(XminMean, 2));
        }
        double sum = sum(squares);
        return sum / (biasCorrected ? (n - 1) : n);

    }
    
    public static boolean isBlank(String str){
    	return !(str != null && str.length() > 0);
    }

}
