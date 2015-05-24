package sjsu.cmpe.cache.client;

public class Client {
	public static void main(String[] args) throws Exception {
		System.out.println("Starting Cache Client...");
		
		System.out.println("Performing First Put.....");
		CRDTClient.put(1,"a");
		Thread.sleep(30000);	
		System.out.println("Performing Second Put.....");
		CRDTClient.put(1,"b");
		Thread.sleep(30000);
		System.out.println("Performing Get Operation.....");
		CRDTClient.get(1);
		System.out.println("Existing Cache Client...");
	}
}