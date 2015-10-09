package pl.ais.commons.bean.validation.listener;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;
import pl.ais.commons.bean.domain.model.Person;
import pl.ais.commons.bean.validation.ValidationContext;
import pl.ais.commons.bean.validation.event.ValidationListener;

import static pl.ais.commons.bean.validation.Constraints.notBlank;
import static pl.ais.commons.bean.validation.ValidationContext.validationOf;

/**
 * Verifies Spring Validation Listener (SVL) expectations.
 *
 * @author Warlock, AIS.PL
 * @see SpringValidationListener Spring Validation Listener (SVL)
 * @since 1.2.1
 */
public class SpringValidationListenerExpectations {

    /**
     * Verifies if constraint violated by collection of properties' values is reported as global error.
     */
    @Test
    public void shouldReportInvalidCollectionOfPropertiesAsGlobalError() {

        final Person person = new Person();
        person.setName("Eugene Smith");

        final Errors errors = Mockito.mock(Errors.class);
        final ValidationListener listener = new SpringValidationListener(errors);

        try (final ValidationContext<Person> validateThat = validationOf(person).observedBy(listener)) {

            final Person subject = validateThat.subject();

            validateThat.allOf(subject.getName(), subject.getNotes())
                        .satisfies(notBlank().withDescription("Both name and notes should be defined!"));
        }

        Mockito.verify(errors, Mockito.times(1))
               .reject("notBlank", new Object[0], "Both name and notes should be defined!");
    }

    /**
     * Verifies if constraint violated by property value is reported as field error.
     */
    @Test
    public void shouldReportInvalidPropertyAsFieldError() {
        final Person person = new Person();
        person.setName("Eugene Smith");

        final Errors errors = Mockito.mock(Errors.class);
        final ValidationListener listener = new SpringValidationListener(errors);

        try (final ValidationContext<Person> validateThat = validationOf(person).observedBy(listener)) {

            final Person subject = validateThat.subject();

            validateThat.valueOf(subject.getNotes())
                        .satisfies(notBlank().withDescription("Notes should be defined!"));
        }

        Mockito.verify(errors, Mockito.times(1))
               .rejectValue("notes", "notBlank", new Object[0], "Notes should be defined!");
    }

}
