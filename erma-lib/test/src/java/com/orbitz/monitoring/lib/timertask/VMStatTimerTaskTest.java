package com.orbitz.monitoring.lib.timertask;

import java.util.Collection;

import junit.framework.TestCase;

import com.orbitz.monitoring.api.Monitor;

/**
 * Unit tests for the VMStatTimerTask.
 * @author Matt O'Keefe
 */
public class VMStatTimerTaskTest extends TestCase {

    private VMStatTimerTask task;

    protected void setUp() throws Exception {
        task = new VMStatTimerTask();
    }

    public void testVMStats() {
        task.run();
        Collection<Monitor> monitors = task.emitMonitors();
        boolean garbageCollectorStats = false;
        boolean threadStats = false;
        boolean heapMemoryStats = false;
        boolean nonHeapMemoryStats = false;
        boolean finalMemoryStats = false;
        boolean heapMemoryPoolStats = false;
        boolean nonHeapMemoryPoolStats = false;
        for (Monitor monitor: monitors) {
            if("JvmStats".equals(monitor.get(Monitor.NAME))) {
                if (monitor.getAsString("type").startsWith("GarbageCollector")) {
                    garbageCollectorStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                    assertTrue("Didn't find a time attribute", monitor.hasAttribute("time"));
                } else if (monitor.getAsString("type").startsWith("Thread")) {
                    threadStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                } else if (monitor.getAsString("type").startsWith("Memory.Heap.memoryUsage")) {
                    heapMemoryStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                    assertTrue("Didn't find a percent attribute", monitor.hasAttribute("percent"));
                } else if (monitor.getAsString("type").startsWith("Memory.NonHeap.memoryUsage")) {
                    nonHeapMemoryStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                    assertTrue("Didn't find a percent attribute", monitor.hasAttribute("percent"));
                } else if (monitor.getAsString("type").startsWith("Memory.objectPendingFinalization")) {
                    finalMemoryStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                } else if (monitor.getAsString("type").startsWith("Memory.Heap.Pool")) {
                    heapMemoryPoolStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                    if(monitor.getAsString("type").endsWith("memoryUsage")) {
                        assertTrue("Didn't find a percent attribute", monitor.hasAttribute("percent"));
                    }
                } else if (monitor.getAsString("type").startsWith("Memory.NonHeap.Pool")) {
                    nonHeapMemoryPoolStats = true;
                    assertTrue("Didn't find a count attribute", monitor.hasAttribute("count"));
                    if(monitor.getAsString("type").endsWith("memoryUsage")) {
                        assertTrue("Didn't find a percent attribute", monitor.hasAttribute("percent"));
                    }
                }
            }
        }
        assertTrue("Didn't find a GarbageCollector Monitor", garbageCollectorStats);
        assertTrue("Didn't find a Thread Monitor", threadStats);
        assertTrue("Didn't find a Memory.Heap.memoryUsage Monitor", heapMemoryStats);
        assertTrue("Didn't find a Memory.NonHeap.memoryUsage Monitor", nonHeapMemoryStats);
        assertTrue("Didn't find a Memory.objectPendingFinalization Monitor", finalMemoryStats);
        assertTrue("Didn't find a Memory.Heap.Pool Monitor", heapMemoryPoolStats);
        assertTrue("Didn't find a Memory.NonHeap.Pool Monitor", nonHeapMemoryPoolStats);
    }
}
