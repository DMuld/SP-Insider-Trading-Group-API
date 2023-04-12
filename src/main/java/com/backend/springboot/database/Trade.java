package com.backend.springboot.database;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Trade {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	private String TICKER;
	
	private String PUBLISHED;
	
	private String TRADED;
	
	private Integer FILEDAFTER;
	
	private String TYPE;
	
	private Integer VOLUME;
	
	private Float PRICE;

	public String getTICKER() {
		return TICKER;
	}

	public void setTICKER(String tICKER) {
		TICKER = tICKER;
	}

	public String getPUBLISHED() {
		return PUBLISHED;
	}

	public void setPUBLISHED(String pUBLISHED) {
		PUBLISHED = pUBLISHED;
	}

	public String getTRADED() {
		return TRADED;
	}

	public void setTRADED(String tRADED) {
		TRADED = tRADED;
	}

	public Integer getFILEDAFTER() {
		return FILEDAFTER;
	}

	public void setFILEDAFTER(Integer fILEDAFTER) {
		FILEDAFTER = fILEDAFTER;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}

	public Integer getVOLUME() {
		return VOLUME;
	}

	public void setVOLUME(Integer vOLUME) {
		VOLUME = vOLUME;
	}

	public Float getPRICE() {
		return PRICE;
	}

	public void setPRICE(Float pRICE) {
		PRICE = pRICE;
	}
	
	
}
