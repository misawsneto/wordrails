package com.wordrails.api;

import java.io.Serializable;

public class CellView implements Serializable {
	private static final long serialVersionUID = -9007636043152596902L;
	
	public Integer id;
	public int index;
	public PostView postView;
}