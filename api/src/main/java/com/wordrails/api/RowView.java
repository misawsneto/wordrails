package com.wordrails.api;

import java.io.Serializable;
import java.util.List;

public class RowView implements Serializable {
	private static final long serialVersionUID = 8344796806642658494L;
	
	public Integer id;
	public Integer termId;
	public String type;
	public Integer index;
	public Integer termPerspectiveId;
	public List<CellView> cells;
}