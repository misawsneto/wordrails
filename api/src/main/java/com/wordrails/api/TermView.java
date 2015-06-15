package com.wordrails.api;

import java.io.Serializable;

public class TermView implements Serializable {
	private static final long serialVersionUID = 5504321793935627049L;
	
	public Integer termId;
	public String termName;
	public Integer parentId;
}