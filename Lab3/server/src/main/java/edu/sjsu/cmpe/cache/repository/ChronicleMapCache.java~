package edu.sjsu.cmpe.cache.repository;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import edu.sjsu.cmpe.cache.domain.Entry;
import net.openhft.chronicle.map.ChronicleMap;

public class ChronicleMapCache implements CacheInterface {

	private final ChronicleMap<Long,Entry>chronicle;
	
	public ChronicleMapCache(ChronicleMap<Long,Entry>entries){
		chronicle=entries;
	}
	
	public Entry save(Entry newEntry) {
		// TODO Auto-generated method stub
		 checkNotNull(newEntry, "The New Entry instance should Not Be Null!!");
		 chronicle.putIfAbsent(newEntry.getKey(), newEntry);
		return null;
	}

	public Entry get(Long key) {
		// TODO Auto-generated method stub
		checkArgument(key > 0,
                "The Key %s was should be greater than zero value: The Value Is", key);
		return chronicle.get(key);
	}
	public List<Entry> getAll() {
		// TODO Auto-generated method stub
		return new ArrayList<Entry>(chronicle.values());
	}
}
