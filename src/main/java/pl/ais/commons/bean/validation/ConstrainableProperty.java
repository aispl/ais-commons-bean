package pl.ais.commons.bean.validation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import pl.ais.commons.bean.util.Path;

/**
 * Constrainable property.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@Immutable
public final class ConstrainableProperty<V> extends AbstractConstrainable<V> {

    private transient final Path path;

    /**
     * Constructs new instance.
     *
     * @param value the property value
     * @param path the property path
     */
    public ConstrainableProperty(@Nullable final V value, @Nonnull final Path path) {
        super(value);
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <R> R accept(@Nonnull final ConstrainableVisitor<?> visitor) {
        return (R) visitor.visit(this);
    }

    /**
     * @return the property path
     */
    @Nonnull
    public Path getPath() {
        return path;
    }

    /**
     * @return the path representation
     */
    @Nonnull
    public String getPathRepresentation() {
        return path.getRepresentation();
    }

}