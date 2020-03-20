package com.amazonaws.shortify;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Index;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.NameMap;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class GetLongUrl implements RequestHandler<Output, Input> {
	AmazonDynamoDB dbinstance = AmazonDynamoDBClientBuilder.defaultClient();
	DynamoDB dynamoDB = new DynamoDB(dbinstance);
	Table table = dynamoDB.getTable("ShortifyV1");
	
	@Override
	public Input handleRequest(Output input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("\n***********************************************************\n");
		
		Index index = table.getIndex("base62Id-index");
		QuerySpec querySpec = new QuerySpec();
		ItemCollection<QueryOutcome> items = null;
		List<String> result = new ArrayList<>();
		
		querySpec.withKeyConditionExpression("#id = :shortId")
					.withNameMap(new NameMap().with("#id", "base62Id"))
					.withValueMap(new ValueMap().withString(":shortId", input.getShortenedUrl()))
					.withProjectionExpression("OriginalUrl");
		
		items = index.query(querySpec);
		
		
		Iterator<Item> iterator = items.iterator();
		Input res = new Input();
		
		logger.log("\nQuery: printing results...\n");

		while (iterator.hasNext()) {
			res.setOriginalUrl(iterator.next().getString("OriginalUrl"));
			logger.log("\naccessed Url:" + res.getOriginalUrl()+ "\n");
			throw new ResponseFound(res.getOriginalUrl());
		}
		
		logger.log("\nGSI results: " + result);
		
		throw new ResponseNotFound("<html><h1>404 Not Found</h1></html>");
		
	}
	
}
