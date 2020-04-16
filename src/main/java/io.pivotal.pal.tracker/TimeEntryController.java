package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    private TimeEntryRepository timeEntryRepository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;
    public TimeEntryController(TimeEntryRepository timeEntryRepository,
                               MeterRegistry meterRegistry) {
        this.timeEntryRepository = timeEntryRepository;
        timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }
    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        try {
            TimeEntry result = this.timeEntryRepository.create(timeEntry);
            actionCounter.increment();
            timeEntrySummary.record(this.timeEntryRepository.list().size());
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") long timeEntryId) {
        try {
            TimeEntry t = this.timeEntryRepository.find(timeEntryId);
            if (t != null) {
                actionCounter.increment();
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        try {
            actionCounter.increment();
            return new ResponseEntity<>(this.timeEntryRepository.list(), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") long id, @RequestBody TimeEntry expected) {
        try {
            TimeEntry t = this.timeEntryRepository.update(id, expected);
            if (t != null) {
                actionCounter.increment();
                return new ResponseEntity<>(t, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") long id) {
        try {
            this.timeEntryRepository.delete(id);
            actionCounter.increment();
            timeEntrySummary.record(this.timeEntryRepository.list().size());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
