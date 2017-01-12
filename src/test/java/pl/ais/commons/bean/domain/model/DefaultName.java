package pl.ais.commons.bean.domain.model;

/**
 * @author Warlock, AIS.PL
 * @since 1.3.3
 */
public final class DefaultName implements Name {

    private final String firstName;

    private final String lastname;

    public DefaultName(final String firstName, final String lastname) {
        this.firstName = firstName;
        this.lastname = lastname;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastname;
    }

}
