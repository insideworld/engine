package insideworld.engine.quarkus.threads;

import insideworld.engine.threads.ThreadService;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.microprofile.context.ThreadContext;

@Singleton
public class QuarkusThreadService implements ThreadService {

    private final ThreadContext context;

    @Inject
    public QuarkusThreadService(final ThreadContext context) {
        this.context = context;
    }

    @Override
    public <T> Future<T> newThread(final Callable<T> callable, final ExecutorService executor) {
        return executor.submit(this.context.contextualCallable(callable));
    }

}
