package pl.ais.commons.bean.validation.constrainable;

import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * Defines the API contract for Constrainable visitor.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.1
 */
public interface ConstrainableVisitor<R> {

    /**
     * Visits given constrainable value.
     *
     * @param constrainable the constrainable value to be visited
     * @return value specific for the concrete visitor implementation, can be {@code null}
     */
    @CheckForNull
    R visit(@Nonnull ConstrainableValue<?> constrainable);

    /**
     * Visits given constrainable group.
     *
     * @param constrainable the constrainable group to be visited
     * @return value specific for the concrete visitor implementation, can be {@code null}
     */
    @CheckForNull
    R visit(@Nonnull ConstrainableGroup<?> constrainable);

}
