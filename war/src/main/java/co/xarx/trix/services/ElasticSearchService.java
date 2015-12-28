package co.xarx.trix.services;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

public class ElasticSearchService {

	protected Client client;

	public Client getClient() {
		return client;
	}


	public ElasticSearchService(String cluster, String host, Integer port, String user, String password){
        port = 9300;
		startEsClient(cluster, host, port, user, password);
	}

	public Client getElasticsearchClient(){
		return client;
	}

	private void startEsClient(String cluster, String host, Integer port, String user, String password){
		Settings settings = ImmutableSettings.settingsBuilder()
				.put("cluster.name", cluster)
				.put("shield.user", user + ":" + password)
				.build();

		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	public IndexResponse index(String doc, String id, String index, String type){
		return client == null ? null:  client.prepareIndex(index, type, id)
				.setSource(doc)
				.execute()
				.actionGet();
	}

	public UpdateResponse update(String doc, String id, String index, String type){
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(index);
		updateRequest.type(type);
		updateRequest.id(id);
		updateRequest.doc(doc);
		try {
			return client.update(updateRequest).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void save(String doc, String id, String index, String type){
		index(doc, id, index, type);
	}

	public void delete(String id, String index, String type){
		if(client !=null)
			client.prepareDeleteByQuery(index)
					.setQuery(QueryBuilders.idsQuery(type).addIds(id))
					.execute();
	}
}
