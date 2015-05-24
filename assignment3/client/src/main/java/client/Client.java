package client;

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
		for(int i=0;i<nodes.size();i++){
			//System.out.println(Hashing.md5().hashString(nodes.get(i), Charsets.UTF_8).padToLong());
			servermapping.put(Hashing.md5().hashString(nodes.get(i), Charsets.UTF_8).padToLong(), nodes.get(i));
		}
		ArrayList ob=new ArrayList();
		ob.add('a');
		ob.add('b');
		ob.add('c');
		ob.add('d');
		ob.add('e');
		ob.add('f');
		ob.add('g');
		ob.add('h');
		ob.add('i');
		ob.add('j');
		for(int i=0;i<ob.size();i++){
			String node=getNode(Hashing.md5().hashString(ob.get(i).toString(), Charsets.UTF_8).padToLong(),servermapping);
			//String node=getRHash(ob.get(i).toString());
			CacheServiceInterface cache = new DistributedCacheService(
					node);

			cache.put(i+1, ob.get(i).toString());
			System.out.println("put("+(i+1)+" => " +ob.get(i)+")");
		}
		for(int i=0;i<ob.size();i++){
			//String node=getNode(Hashing.md5().hashString(ob.get(i).toString(), Charsets.UTF_8).padToLong(),servermapping);
			String node=getRandezvousHashValue(ob.get(i).toString());
			CacheServiceInterface cache = new DistributedCacheService(
					node);
			String value=cache.get(i+1);
			System.out.println("get("+(i+1)+") => "+value);
		}
		System.out.println("Existing Cache Client...");
	}
	//Getting consistent hashing
	public static String getNode(Long hashfun,SortedMap<Long,String>servermapping){
		if(!servermapping.containsKey(hashfun)){
			SortedMap<Long, String> tailMap =servermapping.tailMap(hashfun);
			hashfun=tailMap.isEmpty() ? servermapping.firstKey() : tailMap.firstKey();
		}
		return servermapping.get(hashfun);
	}
	//Getting randezvous hashing values
	public static String getRandezvousHashValue(String key) {
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

