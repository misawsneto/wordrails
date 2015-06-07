package com.wordrails.services;

import com.wordrails.business.Image;

public class WordpressParsedContent {

	public String content;
	public String externalImageUrl;
	public Image image;

    public WordpressParsedContent() {
        content = "";
        externalImageUrl = "";
    }
}
