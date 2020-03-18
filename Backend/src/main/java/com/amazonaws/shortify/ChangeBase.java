package com.amazonaws.shortify;

import java.math.BigInteger;

public class ChangeBase {
	private static final char[] b62CharArr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private static final String b62string = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	
	public static String encodeB62(String uuid) {
		resolveNotNull(uuid);
		String hexString = uuid.replaceAll("-", "");
		System.out.println("hexString: "+ hexString);
		
		BigInteger hex = new BigInteger(hexString, 16);
		BigInteger radix = BigInteger.valueOf(62);
		BigInteger zero = BigInteger.ZERO;
		System.out.println("long hex value:" + hex);
		
		String b62 = "";
		while( hex.compareTo(zero) > 0) {
			int rem = hex.mod(radix).intValue();
			b62 = b62CharArr[rem] + b62;
			hex = hex.divide(radix);
		}
		
		return b62;
	}
	
	private static void resolveNotNull(String uuid) {
		if(uuid == null || uuid.equals(""))
			throw new NullPointerException();
	}
	
	private static String decodeB62(String id) {
		 String number = id + "";
		  BigInteger value = BigInteger.ZERO;
		  for (char c : number.toCharArray())
		  {
		    value = value.multiply(BigInteger.valueOf(62)); 
		    if ('0' <= c && c <= '9')
		    {
		      value = value.add(BigInteger.valueOf(c - '0'));
		    }
		    if ('a' <= c && c <= 'z')
		    {
		      value = value.add(BigInteger.valueOf(c - 'a' + 10));
		    }
		    if ('A' <= c && c <= 'Z')
		    {
		      value = value.add(BigInteger.valueOf(c - 'A' + 36));
		    }
		   }
		   return value.toString();
	}
	
	private static String decodeB78360(String url) {
		StringBuilder shortUrl = new StringBuilder(url);
		String newUrl = shortUrl.reverse().toString();
		char[] carr = newUrl.toCharArray();
		
		int index =	b62string.indexOf(carr[3]);
		
		System.out.println("Index: " + index);
		BigInteger value = BigInteger.ZERO;
				
		  for(int i = 0; i < newUrl.length(); i++) {
			  index = b62string.indexOf(carr[i]);
			  value = value.add(BigInteger.valueOf((long) ( Math.pow(62, i))));
		  }
		  System.out.println(value);
		 
		return null;
	}
	
	public static void main(String[] args) {
		String uuid = "087745e0-8e15";
		String shortUrl = encodeB62(uuid);
		System.out.println(shortUrl);
		String decodedValue = decodeB62(shortUrl);
		System.out.println(decodedValue);
	}
}
