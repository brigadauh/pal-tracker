package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Component
public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    HashMap<Long, TimeEntry> inMemoryMap;
    long nextKey = 1;
    public InMemoryTimeEntryRepository() {
        this.inMemoryMap = new HashMap<>();
    }

    public TimeEntry create(TimeEntry timeEntry)   {

        timeEntry.setId(nextKey);
        this.inMemoryMap.put(nextKey, timeEntry);
            this.nextKey++;
            return this.find(timeEntry.getId());
    }

    public TimeEntry find(long id)  {
            return this.inMemoryMap.get(id);
    }

    public List<TimeEntry> list(){
            return new ArrayList<TimeEntry> (this.inMemoryMap.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
            timeEntry.setId(id);
            if (this.inMemoryMap.containsKey(id)) {
                this.inMemoryMap.put(id, timeEntry);
                return this.find(id);
            }
            else {
                return null;
            }
    }

    public void delete(long id)  {
            this.inMemoryMap.remove(id);
    }
}
