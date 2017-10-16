package pl.ais.commons.bean.validation;

import pl.ais.commons.bean.validation.constrainable.Constrainable;
import pl.ais.commons.bean.validation.event.ValidationListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * Defines the API contract for validatable value.
 *
 * @param <T> the type of the validatable values
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public interface Validatable<T> extends Supplier<Constrainable<T>> {

    static <V> Validatable<V> validatable(@Nonnull final Constrainable<V> constrainable,
                                          @Nonnull final ValidationListener listener,
                                          @Nullable final Runnable callback) {
        return new Validatable<V>() {

            /**
             * {@inheritDoc}
             */
            @Override
            public Constrainable<V> get() {
                return constrainable;
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public boolean satisfies(@Nonnull final Constraint<? super V> first, final Constraint<? super V>... rest) {
                try {
                    return first.apply(constrainable, listener) && Arrays.stream(rest)
                                                                         .allMatch(constraint -> constraint.apply(constrainable, listener));
                } finally {
                    if (null != callback) {
                        callback.run();
                    }
                }
            }
        };
    }

    /**
     * Verifies if this validatable satisfies given constraints.
     *
     * @param first constraint which should be satisfied
     * @param rest  remaining constraints which should be satisfied
     * @return {@code true} if all constraints are satisfied by this validatable, {@code false} otherwise
     */
    boolean satisfies(@Nonnull Constraint<? super T> first, Constraint<? super T>... rest);

}
