package pl.ais.commons.bean.validation.constrainable;

import pl.ais.commons.bean.validation.Constraint;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
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

    protected final Collection<T> elements;

    protected ConstrainableCollection(final T first, final T second, final T... rest) {
        elements = new ArrayList<>(2 + rest.length);
        elements.add(first);
        elements.add(second);
        elements.addAll(Arrays.asList(rest));
    }

    /**
     * Creates and returns constrainable over collection of all given values.
     *
     * <p>Constrainable created by this method is satisfying some constraint if and only if all constrainable values
     * enclosed by it are satisfying the constraint.
     *
     * @param <T>    the type of constrainable values
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return constrainable over collection of all given values
     */
    public static <T> Constrainable<T> allOf(final T first, final T second, final T... rest) {
        return new ConstrainableCollection<T>(first, second, rest) {

            /**
             * {@inheritDoc}
             */
            @Override
            public Boolean apply(final Constraint<?, ? super T> constraint) {
                return elements.stream().allMatch(constraint);
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
     * Creates and returns constrainable over any element of given collection values.
     *
     * <p>Constrainable created by this method is satisfying some constraint if and only if any constrainable value
     * enclosed by it is satisfying the constraint.
     *
     * @param <T>    the type of constrainable values
     * @param first  first constrainable value
     * @param second second constrainable value
     * @param rest   remaining constrainable values
     * @return constrainable over any element of given collection of values
     */
    public static <T> Constrainable<T> anyOf(final T first, final T second, final T... rest) {
        return new ConstrainableCollection<T>(first, second, rest) {

            /**
             * {@inheritDoc}
             */
            @Override
            public Boolean apply(final Constraint<?, ? super T> constraint) {
                return elements.stream().anyMatch(constraint);
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
    public <R> R accept(@Nonnull final ConstrainableVisitor visitor) {
        return (R) visitor.visit(this);
    }

}
