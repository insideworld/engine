package insideworld.engine.threads;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public interface ThreadPool {

    <T> Future<T> execute(Callable<T> callable);

}
