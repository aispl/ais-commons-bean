package pl.ais.commons.bean.validation.constrainable;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Collection of constrainable values.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
@SuppressWarnings("PMD.AbstractNaming")
public abstract class ConstrainableCollection<T> implements Constrainable<T> {

    protected final Collection<? extends T> elements;

    protected ConstrainableCollection(@Nonnull final Collection<? extends T> elements) {
        this.elements = new ArrayList<>(elements);
    }

    /**
     * Creates and returns constrainable over given elements.
     *
     * <p>Constrainable created by this method is satisfying some constraint if and only if all constrainable values
     * enclosed by it are satisfying the constraint.
     *
     * @param <T>      the type of constrainable values
     * @param elements elements to be constrained
     * @return constrainable over given elements
     */
    public static <T> Constrainable<T> allOf(final Collection<? extends T> elements) {
        return new ConstrainableCollection<T>(elements) {

            /**
             * {@inheritDoc}
             */
            @Override
            public Boolean apply(final Constraint<?, ? super T> constraint) {
                return elements.stream()
                               .allMatch(constraint::test);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return elements.stream()
                               .map(element -> (null == element) ? "null" : "'" + element + "'")
                               .collect(Collectors.joining(", ", "All values of Collection composed of: [", "]"));
            }

        };
    }

    /**
     * Creates and returns constrainable over given elements.
     *
     * <p>Constrainable created by this method is satisfying some constraint if and only if any constrainable value
     * enclosed by it is satisfying the constraint.
     *
     * @param <T>      the type of constrainable values
     * @param elements elements to be constrained
     * @return constrainable over any element of given collection of values
     */
    public static <T> Constrainable<T> anyOf(final Collection<? extends T> elements) {
        return new ConstrainableCollection<T>(elements) {

            /**
             * {@inheritDoc}
             */
            @Override
            public Boolean apply(final Constraint<?, ? super T> constraint) {
                return elements.stream()
                               .anyMatch(constraint::test);
            }

            /**
             * {@inheritDoc}
             */
            @Override
            public String toString() {
                return elements.stream()
                               .map(element -> (null == element) ? "null" : "'" + element + "'")
                               .collect(Collectors.joining(", ", "Any value from Collection composed of : [", "]"));
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> R accept(@Nonnull final ConstrainableVisitor<R> visitor) {
        return visitor.visit(this);
    }

}
