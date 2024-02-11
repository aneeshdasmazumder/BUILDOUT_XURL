package com.crio.shorturl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class XUrlImpl implements XUrl {

    private static Map<String, String> longToShortUrls = new HashMap<>();
    private static Map<String, Map<String, Integer>> shortToLongUrls = new HashMap<>();
    private String shortUrl = "http://short.url/";

    @Override
    public String registerNewUrl(String longUrl) {
        
        if(longToShortUrls.containsKey(longUrl)) {
            return longToShortUrls.get(longUrl);
        }
        String url = generateRandomString();
        while(shortToLongUrls.containsKey(url)) {
            url = generateRandomString();
        }
        longToShortUrls.put(longUrl, this.shortUrl+url);
        Map<String, Integer> tempHashMap = new HashMap<>();
        tempHashMap.put(longUrl, 0);
        shortToLongUrls.put(longToShortUrls.get(longUrl), tempHashMap);
        return longToShortUrls.get(longUrl);
    }

    @Override
    public String registerNewUrl(String longUrl, String shortUrl) throws NoSuchFieldException {
        if(longToShortUrls.containsKey(longUrl) && longToShortUrls.get(longUrl) == shortUrl) {
            return longToShortUrls.get(longUrl);
        }

        if(shortToLongUrls.containsKey(shortUrl)) {
            Map<String, Integer> tempHashMap = shortToLongUrls.get(shortUrl);
            Set<String> keys = tempHashMap.keySet();
            for(String key : keys) {
                if(key != longUrl)
                    throw new NoSuchFieldException("The Short URL"+shortUrl+" already exists for another Long URL.");
            }
        }
        longToShortUrls.put(longUrl, shortUrl);
        Map<String, Integer> url = new HashMap<>();
        url.put(longUrl, 0);
        shortToLongUrls.put(longToShortUrls.get(longUrl), url);
        return longToShortUrls.get(longUrl);
    }

    @Override
    public String getUrl(String shortUrl) throws NoSuchFieldException {
        if(!shortToLongUrls.containsKey(shortUrl)) {
            throw new NoSuchFieldException("The requested URL doesn't exists");
        }
        String longURL = null;
        
        Map<String, Integer> tempHashMap = shortToLongUrls.get(shortUrl);
        for(Map.Entry<String, Integer> key : tempHashMap.entrySet()) {
            longURL =  key.getKey();
        }
        int count = tempHashMap.get(longURL);
        tempHashMap.put(longURL, count+1);
        return longURL;
    }

    @Override
    public Integer getHitCount(String longUrl) {
        if(!longToShortUrls.containsKey(longUrl)) {
            return 0;
        }
        String shortURL = longToShortUrls.get(longUrl);
        Integer count = 0;
        Map<String, Integer> tempHashMap = shortToLongUrls.get(shortURL);
        for(Map.Entry<String, Integer> key : tempHashMap.entrySet()) {
            count =  key.getValue();
        }

        return count;
    }

    @Override
    public String delete(String longUrl) {
        if(longToShortUrls.containsKey(longUrl)) {
            String shortURL = longToShortUrls.get(longUrl);
            longToShortUrls.remove(longUrl);
            shortToLongUrls.remove(shortURL);
            return "Successfully deleted!!";
        }
        return "LongURL doesn't exists!!";
    }

    private String generateRandomString() {
        String randomstring = UUID.randomUUID().toString().replaceAll("-", "");
        return randomstring.substring(0, 9);
    }

}