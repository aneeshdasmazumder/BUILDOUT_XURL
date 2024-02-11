package com.crio.shorturl;

public class XUrlMain {

  public static void main(String[] args) {

    XUrl xUrl = new XUrlImpl();

    // Test the Short Url Implementation by registering long URLs and looking up short URLs
    
    // Register long URLs and get the corresponding short URLs as return values
    String url = xUrl.registerNewUrl("http://abc.com");
    String url1 = xUrl.registerNewUrl("http://abc1.com");
    String url2 = xUrl.registerNewUrl("http://abc2.com");
    String url3 = xUrl.registerNewUrl("http://abc3.com");
    String url4 = xUrl.registerNewUrl("http://abc2.com");  // url4 should be the same as url2
    System.out.println(url);
    System.out.println(url1);
    System.out.println(url2);
    System.out.println(url3);
    System.out.println(url4);

    // Update new URL mapping to a custom short URL
    String url5=null;
    try {
      url5 = xUrl.registerNewUrl("http://abc5.com", "http://short.url/test1");
    } catch (NoSuchFieldException e2) {
      System.err.println("The Short URL: http://short.url/test1 already exists for another Long URL!!");
    }
    String url6=null;
    try {
      url6 = xUrl.registerNewUrl("http://abc6.com", "http://short.url/test2");
    } catch (NoSuchFieldException e2) {
      System.err.println("The Short URL: http://short.url/test2 already exists for another Long URL!!");
    }
    // Try to update new URL to map to existing short URL, should return null
    String urlNull=null;
    try {
      urlNull = xUrl.registerNewUrl("http://abc7.com", url3);
    } catch (NoSuchFieldException e2) {
      System.err.println("The Short URL: "+url3+" already exists for another Long URL!!");
    }
    assert(urlNull == null);

    System.out.println(url5);
    System.out.println(url6);
    System.out.println(urlNull);

    // Test out longURL lookup based on the shortURL input
    try {
      assert(xUrl.getUrl(url).equals("http://abc.com"));
    } catch (NoSuchFieldException e1) {
      System.err.println("No Long URL exists for "+url+"!!");
    }
    try {
      assert(xUrl.getUrl(url2).equals(xUrl.getUrl(url4)));
    } catch (NoSuchFieldException e) {
      System.err.println("No Long URL exists for "+url2+"!!");
    }
    try {
      assert(xUrl.getUrl(url5).equals("http://abc5.com"));
    } catch (NoSuchFieldException e) {
      System.err.println("No Long URL exists for "+url5+"!!");
    }
    
    // Test out getHitCount() for a given long URL. 
    // Here the same long URL has been looked up 2 times as part of url2 & url4
    assert(xUrl.getHitCount("http://abc2.com").equals(2));
    // Try to fetch hit count for a non existent long URL, should return 0 
    assert(xUrl.getHitCount("http://abcn.com").equals(0));

    // From the short URL url1, remove the common section (http://short.url/) and remove any non alphanumeric character
    String choppedUrl = url1.replace("http://short.url/", "").replaceAll("[^A-Za-z0-9]", "");
    System.out.println(choppedUrl);
    // The result should have only alphanumeric characters and be 9 characters long
    assert (choppedUrl.length() == 9);

    // Delete mapping for the long URL and confirm that the short URL lookup for that long URL returns null
    xUrl.delete("http://abc6.com");
    try {
      assert(xUrl.getUrl(url6) == null);
    } catch (NoSuchFieldException e) {
      System.err.println("No Long URL exists for "+url6+"!!");
    }
  }
}

