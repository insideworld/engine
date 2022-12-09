/*
 * Copyright (c) 2022 Anton Eliseev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package insideworld.engine.quarkus.startup;

import insideworld.engine.exception.CommonException;
import insideworld.engine.startup.OnStartUp;
import insideworld.engine.startup.StartUpException;
import io.quarkus.runtime.Startup;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process startup implemented objects after launch application.
 * @see OnStartUp
 * @since 0.14.0
 */
@Startup(3000)
@Singleton
public class ProcessOnStartUp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessOnStartUp.class);
    private final List<OnStartUp> startups;

    @Inject
    public ProcessOnStartUp(final List<OnStartUp> startups) {
        this.startups = startups;
    }

    @PostConstruct
    public void init() throws StartUpException {
        final List<OnStartUp> sorted = this.startups.stream()
            .sorted(Comparator.comparingLong(OnStartUp::startOrder).reversed())
            .toList();
        this.logOrder(sorted);
        for (final OnStartUp start : sorted) {
            try {
                start.startUp();
            } catch (final Exception exp) {
                throw wrap(exp, () -> new StartUpException(exp, start.getClass()), StartUpException.class);
            }
        }
    }



    /**
     * Log order of startup objects.
     * @param sorted Sorted startups objects.
     */
    private void logOrder(final List<OnStartUp> sorted) {
        if (ProcessOnStartUp.LOGGER.isDebugEnabled()) {
            ProcessOnStartUp.LOGGER.debug("Startup objects:");
            for (final OnStartUp start : sorted) {
                ProcessOnStartUp.LOGGER.debug("{} {}", start.startOrder(), start.getClass().getName());
            }
        }
    }

}
