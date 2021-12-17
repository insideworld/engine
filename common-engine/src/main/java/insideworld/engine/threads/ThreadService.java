package insideworld.engine.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public interface ThreadService {

    <T> Future<T> newThread(Callable<T> callable, ExecutorService executor);

}
