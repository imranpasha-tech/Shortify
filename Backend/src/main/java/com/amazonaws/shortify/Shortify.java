package com.amazonaws.shortify;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Instant;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * @author imran.pasha
 *
 */
public class Shortify implements RequestHandler<Input, Output> {
	AmazonDynamoDB dbinstance = AmazonDynamoDBClientBuilder.defaultClient();
	DynamoDB dynamoDB = new DynamoDB(dbinstance);
	private static final String shortifyUrl = "https:/Shorti.fy/";
	
	@Override
	public Output handleRequest(Input input, Context context) {
		LambdaLogger logger = context.getLogger();
		logger.log("\n----------------------Adding new Url to database------------------------");
		logger.log("\nURL details: " + input.getOriginalUrl() + "\n" );
		
		UrlItem item =  new UrlItem();
		item.setOriginalUrl(input.getOriginalUrl());
		
		DynamoDBMapper mapper = new DynamoDBMapper(dbinstance);
		mapper.save(item);
		
		String base62Id = encodeHex(item.getId());
		String shortUrl = shortifyUrl + base62Id;
		
		/**
		 * To-do: Need to find a way to make this consistent and atomic?
		 */
		item.setBase62Id(base62Id);
		item.setCreationDate(new Instant().getMillis());
		item.setExpirationDate(new DateTime().plusYears(5).getMillis());
		mapper.save(item);
		
		System.out.println("Shortified url: " + shortUrl);
		Output o = new Output();
		o.setShortenedUrl(shortifyUrl + item.getBase62Id());
		return o;
	}
	
	/**
	 * base62 encoding for uuid; limited to 8 characters.
	 * @param hexId
	 * @return
	 */
	private String encodeHex(String Id) {
		String trimmedId = Id.substring(0, 14);
		String hexId = trimmedId.replaceAll("-", "");
		String shortUrl = ChangeBase.encodeB62(hexId);
		System.out.println("Short Url is: " + shortUrl);
		
		return shortUrl;
	}
}
