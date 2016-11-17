package org.ssr.formula;

public interface FormulaSectionBase {

	public Double getXVaule(Double x);
	
	public Double getYValue(Double y);

	public static class Section1 implements FormulaSectionBase {

		@Override
		public Double getXVaule(Double x) {
			return x;
		}

		@Override
		public Double getYValue(Double y) {
			return y;
		}
		
	}
}
