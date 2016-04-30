package org.ssr.bo;

import java.text.DecimalFormat;

import org.ssr.components.ReaderComponents;

public class StandardDetailsBO {
	Long stdId;
	Double stdValue;
	Double rluValue;
	public Long getStdId() {
		return stdId;
	}
	public void setStdId(Long stdId) {
		this.stdId = stdId;
	}
	public Double getStdValue() {
		return stdValue;
	}
	public void setStdValue(Double stdValue) {
		this.stdValue = stdValue;
	}
	public Double getRluValue() {
		return rluValue != null ? rluValue : 0;
	}
	
	public void setRluValue(Double rluValue) {
		this.rluValue = rluValue;
	}

    public String getRluValueString()
    {
        if(ReaderComponents.getMode().equals("ELISA"))
        {
            DecimalFormat df = new DecimalFormat("#.000");
            df.format(rluValue);
        }
        return String.valueOf(rluValue);
    }

}
