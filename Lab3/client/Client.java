package sjsu.cmpe.cache.client;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class Client {
	
	 private static final Funnel<CharSequence> strFunnel = Funnels.stringFunnel(Charset.defaultCharset());
	 private final static  HashFunction hasher=Hashing.md5();
	 public static ArrayList<String>nodes=new ArrayList<String>();

    public static void main(String[] args) throws Exception {
         System.out.println("Starting Cache Client...");
        
       
        SortedMap<Long,String>servermapping=new TreeMap<Long,String>();
        
        nodes.add("http://localhost:3000");
        nodes.add("http://localhost:3001");
        nodes.add("http://localhost:3002");
       // System.out.println("*****server hash code ***************");
        
        for(int i=0;i<nodes.size();i++){
        	//System.out.println(Hashing.md5().hashString(nodes.get(i), Charsets.UTF_8).padToLong());
     	   servermapping.put(Hashing.md5().hashString(nodes.get(i), Charsets.UTF_8).padToLong(), nodes.get(i));
        }
        
        ArrayList objects=new ArrayList ();
        
        
        objects.add('a');
        objects.add('b');
        objects.add('c');
        objects.add('d');
        objects.add('e');
        objects.add('f');
        objects.add('g');
        objects.add('h');
        objects.add('i');
        objects.add('j');
        for(int i=0;i<objects.size();i++){
     	   
        	//String node=getNode(Hashing.md5().hashString(objects.get(i).toString(), Charsets.UTF_8).padToLong(),servermapping);
      	
        	
        	String node=getRHash(objects.get(i).toString());
        	
        	
        	CacheServiceInterface cache = new DistributedCacheService(
        			   node);
        	 
        	   cache.put(i+1, objects.get(i).toString());
               System.out.println("put("+(i+1)+" => " +objects.get(i)+")");
        }
        
        //System.out.println("*****objects hash code ***************");
        for(int i=0;i<objects.size();i++){
      	   
        	
        	//String node=getNode(Hashing.md5().hashString(objects.get(i).toString(), Charsets.UTF_8).padToLong(),servermapping);
        	String node=getRHash(objects.get(i).toString());
        	   CacheServiceInterface cache = new DistributedCacheService(
        			   node);
        	   String value=cache.get(i+1);
               System.out.println("get("+(i+1)+") => "+value);
        }
        System.out.println("Existing Cache Client...");
    }

    /**
     * Getting Node using Consistent hashing 
     * @param hashfun
     * @param servermapping
     * @return
     */
public static String getNode(Long hashfun,SortedMap<Long,String>servermapping){
		
		
		if(!servermapping.containsKey(hashfun)){
			SortedMap<Long, String> tailMap =servermapping.tailMap(hashfun);
			
			hashfun=tailMap.isEmpty() ? servermapping.firstKey() : tailMap.firstKey();
			
		}
		
		return servermapping.get(hashfun);
	}


/**
 * Getting the node value from Rendezvous or Highest Random Weight (HRW) hashing
 * @param key
 * @return
 */
public static String getRHash(String key) {
	long maxValue = Long.MIN_VALUE;
	String max = null;
	for (String node : nodes) {
		long nodesHash = hasher.newHasher()
				.putObject(key, strFunnel)
				.putObject(node, strFunnel)
				.hash().asLong();
		if (nodesHash > maxValue) {
			max = node;
			maxValue = nodesHash;
		}
	}
	return max;
}


}

