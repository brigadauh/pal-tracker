package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    HashMap<Long, TimeEntry> inMemoryMap;
    long nextKey = 1;
    public InMemoryTimeEntryRepository() {
        this.inMemoryMap = new HashMap<>();
    }
    @Override
    public TimeEntry create(TimeEntry timeEntry)   {
        TimeEntry newTimeEntry = new TimeEntry(nextKey, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(),timeEntry.getHours());
        this.inMemoryMap.put(nextKey, newTimeEntry);
            this.nextKey++;
            return this.find(newTimeEntry.getId());
    }
    @Override
    public TimeEntry find(long id)  {
            return this.inMemoryMap.get(id);
    }
    @Override
    public List<TimeEntry> list(){
            return new ArrayList<TimeEntry> (this.inMemoryMap.values());
    }
    @Override
    public TimeEntry update(long id, TimeEntry timeEntry) {
        TimeEntry newTimeEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(),timeEntry.getHours());
        if (this.inMemoryMap.containsKey(id)) {
                this.inMemoryMap.replace(id, newTimeEntry);
                return this.find(id);
            }
            else {
                return null;
            }
    }
    @Override
    public void delete(long id)  {
            this.inMemoryMap.remove(id);
    }
}
