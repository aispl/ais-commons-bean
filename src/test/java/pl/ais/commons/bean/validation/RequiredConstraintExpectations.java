package pl.ais.commons.bean.validation;

import org.junit.Test;
import org.mockito.Mockito;
import pl.ais.commons.bean.domain.model.Person;
import pl.ais.commons.bean.validation.event.ConstraintViolated;
import pl.ais.commons.bean.validation.event.ValidationListener;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static pl.ais.commons.bean.validation.Constraints.required;
import static pl.ais.commons.bean.validation.ValidationContext.validationOf;

/**
 * Verifies the behaviour of constraint returned by {@link Constraints#required()} method.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.2
 */
public class RequiredConstraintExpectations {

    /**
     * Verifies if contraint violation is reported when validated value is undefined.
     */
    @Test
    public void shouldReportConstraintViolationWhenValueIsUndefined() {

        final Person entity = new Person();

        final ValidationListener listener = Mockito.mock(ValidationListener.class);

        try (final ValidationContext<Person> validateThat = validationOf(entity).observedBy(listener)) {
            final Person subject = validateThat.subject();

            final Validatable<LocalDate> dateOfBirth = validateThat.valueOf(subject.getDateOfBirth());
            dateOfBirth.satisfies(required());

            verify(listener).constraintViolated(new ConstraintViolated(required(), dateOfBirth.get()));
            verifyNoMoreInteractions(listener);
        }

    }

}
