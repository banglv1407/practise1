package com.bang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.opencsv.CSVReader;

public class HttpClient {

    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static void main(String[] args) throws Exception {

        HttpClient obj = new HttpClient();

        try {
//            System.out.println("Testing 1 - Send Http GET request");
//            obj.sendGet();

            System.out.println("Send Http POST request");
            obj.sendPost();
        } finally {
            obj.close();
        }
    }

    private void close() throws IOException {
        httpClient.close();
    }

    private void sendGet() throws Exception {

        HttpGet request = new HttpGet("https://www.google.com/search?q=mkyong");

        // add request headers
        request.addHeader("custom-key", "mkyong");
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");

        try (CloseableHttpResponse response = httpClient.execute(request)) {

            // Get HttpResponse Status
            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                // return it as a String
                String result = EntityUtils.toString(entity);
                System.out.println(result);
            }

        }

    }

    private void sendPost() throws Exception {
    	List<String> arrayCreate = new ArrayList<String>();
    	String csvFile = "C:/Users/Thanh/Desktop/khachhangsaonghe2.csv";
    	CSVReader reader = null;
    	try {
//    		String companyId = "TC03i1IDj8ySrWg";
            reader = new CSVReader(new FileReader(csvFile));
            String[] line = reader.readNext();
            while ((line = reader.readNext()) != null) {
            	String fullName = line[0].trim();
            	String phoneNumber = line[1];
            	String email = line[2];
            	String dateOfBirth = line[3];
            	
            	String order = line[4];
            	order = order.replaceAll("[^0-9|]", "");
            	order = order.split("\\|")[1];
            	
            	String payed = line[5];
            	payed = payed.replaceAll("[^0-9|]", "");
            	payed = payed.split("\\|")[1];
            	
            	String cancel = line[6];
            	cancel = cancel.replaceAll("[^0-9|]", "");
            	cancel = cancel.split("\\|")[1];
            	
            	String point = line[7];
            	String totalMoney = line[8];
            	totalMoney = totalMoney.replaceAll("[^a-zA-Z0-9|]", "");
            	totalMoney = totalMoney.split("\\|")[1];
            	
            	phoneNumber = phoneNumber.replaceAll("[^a-zA-Z0-9]", "");
//            	int totalMoney = 0;
//            	if (!line[4].toString().equals("")) {
//            		totalMoney = Integer.parseInt(line[4])*1000;
//            	}
            	if (!phoneNumber.isEmpty()) {
	//                arrayCreate.add("{\"phoneNumber\":"+"\""+phoneNumber+"\""+","+"\"companyId\":"+"\"TC0Fo1t8ynfC7irD\""+","+"\"fullName\":"+"\""+line[1]+"\""+","+"\"address\":"+line[2]+","+"\"totalMoney\":"+totalMoney+"}");
	            	arrayCreate.add("{\"phoneNumber\":"+"\""+phoneNumber+"\""+","
	            			+"\"companyId\":"+"\"TC0Gn1tXAvehpjpW\""+","
	            			+"\"fullName\":"+"\""+fullName+"\""+","
	            			+"\"numberOrder\":"+order+","
	            			+"\"numberOfTickets\":"+payed+","
	            			+"\"numberCancelTicket\":"+cancel+","
	            			+"\"totalMoney\":"+totalMoney+"}");
            		}
            	}
            
//            System.out.println(arrayCreate.toString());
            int chunk = 100;
            int count = 0;
			List<String> temparray;
			List<String> temparrayRest;
			int modArraySizeRound = arrayCreate.size()%chunk;
            for (int i=0,j=arrayCreate.size()-modArraySizeRound; i<j; i+=chunk) {
//            	if (count>401) {
	                temparray = arrayCreate.subList(i,i+chunk);
	                System.out.println(count*chunk+": "+temparray.toString());
	                callAPI(temparray);
//                }
            	count++;
            }
            temparrayRest = arrayCreate.subList(count*chunk,arrayCreate.size());
            System.out.println(temparrayRest.toString());
            callAPI(temparrayRest);
           System.out.println("done");
            
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
    private void callAPI(List<String> array) throws Exception {
      URL url = new URL("https://ticket-dot-dobody-anvui.an.r.appspot.com/dataUpdate/createCustomer");
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);
      connection.setRequestProperty("Content-Type", "application/json");

      OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());  
      out.write(array.toString());
      out.flush();
      out.close();
      
      int res = connection.getResponseCode();

      System.out.println(res);


      InputStream is = connection.getInputStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));
      String line = null;
      while((line = br.readLine() ) != null) {
          System.out.println(line);
      }
      connection.disconnect();
    }

}
