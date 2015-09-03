package pl.ais.commons.bean.validation.listener;

import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
public class PrintingListener implements ValidationListener {

    private PrintStream stream;

    public PrintingListener(final OutputStream stream) {
        this.stream = new PrintStream(stream, true);
    }

    @Override
    public void constraintViolated(@Nonnull final ConstraintViolated event) {
        stream.println(event);
    }

}
