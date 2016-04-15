input {
	file {
		type => "nginx_access"
		path => ["/var/log/nginx/**"]
		exclude => ["*.gz", "error.*"]
	 	start_position => beginning
	 	sincedb_path => "/dev/null"
 	}
}
filter {
	mutate {
		gsub => ["message", "\"", ""]
	}

	grok {
		match => [ "message" , "%{COMBINEDAPACHELOG}+%{GREEDYDATA:extra_fields}"]
		overwrite => [ "message" ]
	}

	#Removes any request with dots
 	if[request] =~ /\w*\./ {
		drop { }
	}

	mutate {
		convert => ["response", "integer"]
		convert => ["bytes", "integer"]
		convert => ["responsetime", "float"]
	}

	geoip {
		source => "clientip"
		target => "geoip"
		add_tag => [ "nginx-geoip" ]
	}

	date {
		match => [ "timestamp" , "dd/MMM/YYYY:HH:mm:ss Z" ]
	}

	kv {
		field_split => "&? "
		source => "message"
		include_keys => ["slug", "username", "stationId", "tags", "q", "projection"]
		trim => "<>\[\],\/\""
		trimkey => "<>\[\],\/\""
	}

	#Read post
	if[request] =~ /\/post\?id=\d+/ {
		ruby {
			code => "event['postId'] = event['request'].sub('/post?id=', '')"
		}
	}

	#Read body from post
	if[request] =~ /\/api\/posts\/\d+\/body/ {
		ruby {
			code => "event['postId'] = event['request'].sub('/api/posts/', '').sub('/body', '')"
		}
	}

	#Read post
	if[request] =~ /\/api\/posts\/\d+[^\/]/ {
		ruby {
			code => "event['postId'] = event['request'].gsub(/[^0-9]/, '')"
		}
	}

	if [major]{
		useragent {
			source => "agent"
			add_field => { "browser" => "%{name}" }
			add_field => { "browser_version" => "%{major}" }
		}
	} else {
		useragent {
			source => "agent"
			add_field => { "browser" => "%{name}" }
		}
	}

	if[slug]{
		mutate {
		rename => {"slug" => "postSlug"}
		}
	}

	#------------- get post using the post's slug -------------#
	if[postSlug]{
		ruby {
			code => "require 'elasticsearch'
				client = Elasticsearch::Client.new log: false
				result = client.search index: event['@metadata']['ES_INDEX'], body: {query:{bool:{must:[{term:{slug:event['postSlug']}}]}}}
				if result['hits']['hits'].length > 0
					event['authorId'] = result['hits']['hits'][0]['_source']['authorId']
					event['postId'] = result['hits']['hits'][0]['_source']['id']
					event['stationId'] = result['hits']['hits'][0]['_source']['stationId']
					event['tenantId'] = result['hits']['hits'][0]['_source']['tenantId']
				else
					puts 'ERROR'
					puts result
				end
				"
		}
	}

	#------------- get post using the post's id -------------#
	if[postId] {
		ruby {
			code => "require 'elasticsearch'
				client = Elasticsearch::Client.new log: false
				result = client.search index: event['@metadata']['ES_INDEX'], body: {query:{bool:{must:[{term:{id:event['postId']}},{term:{type:'post'}}]}}}
				if result['hits']['hits'].length > 0
					event['authorId'] = result['hits']['hits'][0]['_source']['authorId']
					event['postSlug'] = result['hits']['hits'][0]['_source']['slug']
					event['stationId'] = result['hits']['hits'][0]['_source']['stationId']
					event['tenantId'] = result['hits']['hits'][0]['_source']['tenantId']
				else
					puts 'ERROR'
					puts result
				end
				"
		}
	}

	#------------- REMOVED INFO -------------#
	mutate {
		remove_field => ["path", "@version", "os", "patch", "minor", "name", "agent", "timestamp", "ident", "auth", "major", "ES_INDEX"]
	}
}
output {
	elasticsearch {
	    action => "index"
	    #------------- HOST and PORT -------------#
	    hosts => "localhost:9200"
	    #------------- INDEX NAME -------------#
	    index => "%{type}"
	    workers => 2
	}
}