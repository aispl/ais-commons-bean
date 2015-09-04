package pl.ais.commons.bean.validation.constraint;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Base class to be extended by constraints.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
abstract class AbstractConstraint<C extends Constraint<C, T>, T> implements Constraint<C, T> {

    protected final boolean active;

    protected final String message;

    protected final Object[] messageParameters;

    protected final String name;

    protected AbstractConstraint(@Nonnull final String name, final boolean active,
                                 @Nonnull final Object[] messageParameters, @Nullable final String message) {
        super();
        this.name = name;
        this.message = message;
        this.messageParameters = Arrays.copyOf(messageParameters, messageParameters.length);
        this.active = active;
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     */
    public Object[] getMessageParameters() {
        return messageParameters;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isActive() {
        return active;
    }

}
