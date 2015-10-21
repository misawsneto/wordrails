package com.wordrails.api;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.io.Serializable;
import java.util.List;

public class TermView implements Serializable {
	private static final long serialVersionUID = 5504321793935627049L;

	public Integer termId;
	public String termName;
	public Integer parentId;
	public Integer taxonomyId;
	public String imageHash;
	@JsonManagedReference
	public List<TermView> children;
}