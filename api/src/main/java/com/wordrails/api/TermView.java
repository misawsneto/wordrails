package com.wordrails.api;

import java.io.Serializable;
import java.util.List;

public class TermView implements Serializable {
	private static final long serialVersionUID = 5504321793935627049L;
	
	public Integer termId;
	public String termName;
	public RowView splashedRow;
	public List<RowView> ordinaryRows;
	public RowView featuredRow;
}