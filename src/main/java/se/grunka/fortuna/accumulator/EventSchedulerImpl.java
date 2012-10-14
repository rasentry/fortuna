package se.grunka.fortuna.accumulator;

import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class EventSchedulerImpl implements EventScheduler {
    private final AtomicBoolean scheduled = new AtomicBoolean(false);
    private final int sourceId;
    private final Map<Integer, EventContext> eventContexts;
    private final ScheduledExecutorService scheduler;

    public EventSchedulerImpl(int sourceId, Map<Integer, EventContext> eventContexts, ScheduledExecutorService scheduler) {
        this.sourceId = sourceId;
        this.eventContexts = eventContexts;
        this.scheduler = scheduler;
    }

    @Override
    public void schedule(long delay, TimeUnit timeUnit) {
        scheduled.set(true);
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                EventContext eventContext = eventContexts.get(sourceId);
                scheduled.set(false);
                eventContext.source.event(eventContext.scheduler, eventContext.adder);
                if (!scheduled.get()) {
                    scheduler.schedule(this, 0, TimeUnit.MILLISECONDS);
                }
            }
        }, delay, timeUnit);
    }
}
