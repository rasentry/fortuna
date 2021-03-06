package com.grunka.random.fortuna.entropy;

import com.grunka.random.fortuna.accumulator.EventAdder;
import com.grunka.random.fortuna.accumulator.EventScheduler;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

public class ThreadTimeEntropySourceTest {

    private ThreadTimeEntropySource target;
    private int schedules, adds;

    @Before
    public void before() throws Exception {
        target = new ThreadTimeEntropySource();
        schedules = 0;
        adds = 0;
    }

    @Test
    public void shouldAddBytes() throws Exception {
        target.event(
                new EventScheduler() {
                    @Override
                    public void schedule(long delay, TimeUnit timeUnit) {
                        assertEquals(100, timeUnit.toMillis(delay));
                        schedules++;
                    }
                },
                new EventAdder() {
                    @Override
                    public void add(byte[] event) {
                        assertEquals(2, event.length);
                        adds++;
                    }
                }
        );
        assertEquals(1, schedules);
        assertEquals(1, adds);
    }
}
