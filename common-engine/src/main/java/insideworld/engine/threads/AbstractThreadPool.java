package insideworld.engine.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class AbstractThreadPool implements ThreadPool {

    private final ThreadService service;

    private final ExecutorService executor;

    public AbstractThreadPool(final ThreadService service) {
        this.service = service;
        this.executor = this.createPool();
    }

    @Override
    public <T> Future<T> execute(final Callable<T> callable) {
        return this.service.newThread(callable, this.executor);
    }

    protected abstract ExecutorService createPool();
}
