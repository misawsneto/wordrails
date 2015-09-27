package com.wordrails.elasticsearch;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by jonas on 26/09/15.
 */
abstract class ElasticsearchBaseRepository {

	@Value("${elasticsearch.host}")
	private String host;
	@Value("${elasticsearch.port}")
	private Integer port;

	private static TransportClient transportClient;

	public void createClient(){
		transportClient = new TransportClient().
				addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	public static void closeClient(){
		transportClient.close();
	}

	protected static IndexResponse index(String doc, String id, String index, String type){
		return transportClient.prepareIndex(index, type, id)
				.setSource(doc)
				.execute()
				.actionGet();
	}

	protected void _save(String doc, String id, String index, String type){
		createClient();
		index(doc, id, index, type);
		closeClient();
	}
}
