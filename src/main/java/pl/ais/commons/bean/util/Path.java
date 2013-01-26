package pl.ais.commons.bean.util;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Path.
 *
 * @author Warlock, AIS.PL
 * @since 1.0.1
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.MissingSerialVersionUID"})
public class Path implements Serializable {

    /**
     * Defines the root path.
     */
    public static final Path ROOT = new Path("/");

    private final String current;

    private final Path parent;

    private Path(final Path parent, final String current) {
        this.parent = parent;
        this.current = current;
    }

    /**
     * Constructs new instance.
     *
     * @param current the current path
     */
    public Path(final String current) {
        this(null, current);
    }

    /**
     * @return string representation of this path
     */
    public String getRepresentation() {
        final StringBuilder builder = new StringBuilder();
        if (!equals(ROOT)) {
            if (null != parent) {
                builder.append(parent.getRepresentation());
            }
            if (builder.length() > 0) {
                builder.append(".");
            }
            builder.append(current);
        }
        return builder.toString();
    }

    /**
     * Creates and returns path nested under this one.
     *
     * @param nestedPath the nested path
     * @return newly created nested path
     */
    public Path nestedPath(final String nestedPath) {
        return new Path(this, nestedPath);
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("representation", getRepresentation()).build();
    }

}
