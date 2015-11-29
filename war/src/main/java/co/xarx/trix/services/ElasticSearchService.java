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

import java.util.concurrent.ExecutionException;

public class ElasticSearchService {

	protected Client client;

	public Client getClient() {
		return client;
	}

	public ElasticSearchService(String host, Integer port){
		startEsClient(host, port);
	}

	public Client getElasticsearchClient(){
		return client;
	}

	private void startEsClient(String host, Integer port){
		Settings settings = ImmutableSettings.settingsBuilder().put("cluster.name", "trix").build();
		client = new TransportClient(settings).addTransportAddress(new InetSocketTransportAddress(host, port));
	}

	public IndexResponse index(String doc, String id, String index, String type){
		return client == null ? null : client.prepareIndex(index, type, id).setSource(doc).execute().actionGet();
	}

	public UpdateResponse update(String doc, String id, String index, String type){
		UpdateRequest updateRequest = new UpdateRequest();
		updateRequest.index(index);
		updateRequest.type(type);
		updateRequest.id(id);
		updateRequest.doc(doc);
		try {
			return client.update(updateRequest).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void delete(String id, String index, String type){
		if(client !=null)
			client.prepareDeleteByQuery(index)
					.setQuery(QueryBuilders.idsQuery(type).addIds(id))
					.execute();
	}
}
