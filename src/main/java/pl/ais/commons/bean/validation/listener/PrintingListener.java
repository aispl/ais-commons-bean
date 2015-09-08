package pl.ais.commons.bean.validation.listener;

import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Simple validation listener printing information about each violation into given output stream.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
public final class PrintingListener implements ValidationListener {

    private final PrintStream stream;

    /**
     * Constructs new instance.
     *
     * @param stream stream to be used as output for constraint violations
     */
    public PrintingListener(final OutputStream stream) {
        this.stream = new PrintStream(stream, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void constraintViolated(@Nonnull final ConstraintViolated event) {
        stream.println(event);
    }

}
