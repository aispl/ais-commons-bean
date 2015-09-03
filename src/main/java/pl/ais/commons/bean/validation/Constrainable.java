package pl.ais.commons.bean.validation;

import javax.annotation.concurrent.Immutable;
import java.util.Objects;

/**
 * Defines the API contract for the value capable of being constrained.
 *
 * @param <T> the type of the values being constrained
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class Constrainable<T> {

    private final String path;

    private final T value;

    /**
     * Constructs new instance.
     *
     * @param path  path identifying the value in current validation context
     * @param value value to be constrained
     */
    public Constrainable(final String path, final T value) {
        this.path = path;
        this.value = value;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     */
    @Override
    public boolean equals(final Object object) {
        boolean result = (this == object);
        if (!result && (object instanceof Constrainable)) {
            final Constrainable other = (Constrainable) object;
            result = Objects.equals(path, other.path) && Objects.equals(value, other.value);
        }
        return result;
    }

    /**
     * @return path identifying the value in current validation context
     */
    public String getPath() {
        return path;
    }

    /**
     * @return value itself
     */
    public T getValue() {
        return value;
    }

    /**
     * @return a hash code value for this constrainable value
     */
    @Override
    public int hashCode() {
        return Objects.hash(path, value);
    }

    /**
     * @return a String representation of this constrainable value
     */
    @Override
    public String toString() {
        return new StringBuilder().append("Constrainable value of '")
                                  .append(value)
                                  .append("' (")
                                  .append(path)
                                  .append(')')
                                  .toString();
    }

}
