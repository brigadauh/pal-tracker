package io.pivotal.pal.tracker;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TimeEntryRepository {
    public TimeEntry create(TimeEntry timeEntry) ;
    public TimeEntry find( long timeEntryId);
    public TimeEntry update(long eq, TimeEntry any);
    public List<TimeEntry> list();
    public void delete(long timeEntryId);
}
