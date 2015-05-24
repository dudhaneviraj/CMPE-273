package sjsu.cmpe.cache.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.json.JSONObject;

public class CRDTClient {
	
	
	
	public static void put(int key, String value)
	{
		final CountDownLatch latch1 = new CountDownLatch(3);
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		httpclient.start();	
		try {		
			for(int i=0;i<3;i++)
			{
				final HttpPut request2 = new HttpPut("http://localhost:300"+i+"/cache/"+key+"/"+value+"");
				httpclient.execute(request2, new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response2) {
						latch1.countDown();
						System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
						String body="";
						try {
							body = IOUtils.toString(response2.getEntity().getContent());
						} catch (IOException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						System.out.println(body);
					}

					public void failed(final Exception ex) {

						System.out.println(request2.getRequestLine() + "->" + ex);
					}

					public void cancelled() {
						latch1.countDown();
						System.out.println(request2.getRequestLine() + " cancelled");
					}
				});

			}
			latch1.await(2000,TimeUnit.MILLISECONDS);
			httpclient.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			try
			{

				latch1.await(2000,TimeUnit.MILLISECONDS);
				httpclient.close();
			}
			catch (Exception e2) {
				// TODO: handle exception
			}
			if (latch1.getCount()>1)
			{
				System.out.println("Do Repair");
				System.out.println("Write Unsuccessful");
				remove(key);
			}
		}
	}


	public static void remove(int key)
	{
		final CountDownLatch latch1 = new CountDownLatch(3);
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		httpclient.start();	
		try {
			for(int i=0;i<3;i++)
			{
				final HttpDelete request2 = new HttpDelete("http://localhost:300"+i+"/cache/"+key);

				httpclient.execute(request2, new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response2) {
						latch1.countDown();
						System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
						String body="";
						try {
							body = IOUtils.toString(response2.getEntity().getContent());							
						} catch (IOException e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}

					public void failed(final Exception ex) {

						System.out.println(request2.getRequestLine() + "->" + ex);
					}

					public void cancelled() {
						latch1.countDown();
						System.out.println(request2.getRequestLine() + " cancelled");
					}
				});

			}
			latch1.await(2000,TimeUnit.MILLISECONDS);
			httpclient.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		finally
		{
			try
			{
				latch1.await(2000,TimeUnit.MILLISECONDS);
				httpclient.close();

			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}

	public static ArrayList<String> l=new ArrayList<String>();
	public static void increase(String value)
	{
		if(value!=null)
		{
			l.add(value);
		}
	}
	public static void get(int key)
	{
		final CountDownLatch latch1 = new CountDownLatch(3);
		CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
		httpclient.start();	
		int size=0;
		try {
			for(int i=0;i<3;i++)
			{
				final HttpGet request2 = new HttpGet("http://localhost:300"+i+"/cache/"+key);

				Future<HttpResponse>resp =httpclient.execute(request2, new FutureCallback<HttpResponse>() {
					public void completed(final HttpResponse response2) {
						latch1.countDown();
						System.out.println(request2.getRequestLine() + "->" + response2.getStatusLine());
						String body="";
						try 
						{
							body = IOUtils.toString(response2.getEntity().getContent());	
							JSONObject json=new JSONObject(body);
							increase(json.getString("value"));
						} catch (IOException e) {
							// TODO: handle exception

							e.printStackTrace();
						}
						JSONObject json=new JSONObject(body);
						System.out.println(body);
						l.add(json.getString("value"));
					}
					public void failed(final Exception ex) {
						System.out.println(request2.getRequestLine() + "->" + ex);
					}
					public void cancelled() {
						System.out.println(request2.getRequestLine() + " cancelled");
					}
				});	
			}	
			
			latch1.await(2000,TimeUnit.MILLISECONDS);
			Thread.sleep(2100);
			size=l.size();
			//Read Repair
			String temp=majority();
			if(!temp.equals("Nothing"))
			{
				if(size<2)
				{
					l=new ArrayList<String>();
					System.out.println("Majority Servers Not Running...");
					remove(key);
					return;
				}
					System.out.println("Performing Read Repair.........");
					remove(key);
					put(key,temp);
			}				
			else
			{
				System.out.println("All Servers Consistent");
			}
				httpclient.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			finally
			{
				try
				{
					Thread.sleep(2100);
					//Read Repair
					size=l.size();
					String temp=majority();
						if(!temp.equals("Nothing"))
						{
							if(size<2)
							{
								l=new ArrayList<String>();
								System.out.println("Majority Servers Not Running...");
								remove(key);
								return;
							}
							System.out.println("Performing Read Repair....");
							remove(key);
							put(key,temp);
						}	
						else
						{
							
							System.out.println("All Servers Consistent");
						}
					latch1.await(2000,TimeUnit.MILLISECONDS);
					httpclient.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		}
		public static String majority()
		{
			Map<String, Integer> stringsCount = new HashMap<String, Integer>();
			
			if(l.size()==0)
			{
				return "Nothing";
			}
			for(String s: l)
			{
				Integer c = stringsCount.get(s);
				if(c == null) c = new Integer(0);
				c++;
				stringsCount.put(s,c);
			}
			Map.Entry<String,Integer> mostRepeated = null;
			for(Map.Entry<String, Integer> e: stringsCount.entrySet())
			{
				if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
					mostRepeated = e;
			}
			if(mostRepeated != null)
			{
				System.out.println("Majority Found: " + mostRepeated.getKey());		
				if(mostRepeated.getValue()==3)
				{
					l=new ArrayList<String>();
					return "Nothing";
				}
			}
			l=new ArrayList<String>();
			return mostRepeated.getKey();
		} 
}
