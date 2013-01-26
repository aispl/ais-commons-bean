package pl.ais.commons.bean.validation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import pl.ais.commons.bean.util.Path;

/**
 * Constrainable property.
 *
 * @param <V> determines the type of constrainable value
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
public final class ConstrainableProperty<V> extends AbstractConstrainable<V> {

    private transient final Path path;

    /**
     * Constructs new instance.
     *
     * @param path the property path
     * @param value the property value
     */
    public ConstrainableProperty(@Nonnull final Path path, @Nullable final Object value) {
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