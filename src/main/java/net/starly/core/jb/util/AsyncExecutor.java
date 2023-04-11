package net.starly.core.jb.util;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AsyncExecutor {

    private static ExecutorService service = null;

    private static ExecutorService getService() {
        if (service == null)
            service = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors(),
                    (
                            new BasicThreadFactory.Builder())
                            .namingPattern("STAsyncThread-%d")
                            .daemon(true)
                            .priority(5).uncaughtExceptionHandler((t, e) -> {
                                Logger.getLogger("Minecraft").log(Level.SEVERE, t.getName() + " Thread 예외 발생.");
                                e.printStackTrace();
                            }).build());

        return service;
    }

    public static void run(Runnable runnable) {
        getService().execute(new ExceptionHandle(runnable));
    }

    public static <T> T submit(Callable<T> callable) {
        try {
            return getService().submit(callable).get();
        } catch (Exception e) {
            return null;
        }
    }

    public static void shutdown() {
        if (service != null && service.isTerminated()) {
            service.shutdown();
            service = null;
        }
    }

    private static class ExceptionHandle implements Runnable {
        private final Runnable runnable;

        public ExceptionHandle(Runnable r) {
            runnable = r;
        }

        @Override
        public void run() {
            try {
                runnable.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
