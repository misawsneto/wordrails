package com.wordrails.elasticsearch;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created by jonas on 26/09/15.
 */
public class ElasticsearchService {

	protected Client transportClient;

	public ElasticsearchService(String host, Integer port){
		startEsClient(host, port);
	}

	public Client getElasticsearchClient(){
		return transportClient;
	}

	private void startEsClient(String host, Integer port){
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "trix").build();
		transportClient = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	public IndexResponse index(String doc, String id, String index, String type){
		return transportClient == null ? null:  transportClient.prepareIndex(index, type, id)
				.setSource(doc)
				.execute()
				.actionGet();
	}

	public void save(String doc, String id, String index, String type){
		index(doc, id, index, type);
	}

	public void delete(String id, String index, String type){
		if(transportClient!=null)
			transportClient.prepareDeleteByQuery(index)
					.setQuery(QueryBuilders.idsQuery(type).addIds(id))
					.execute();
	}
}
