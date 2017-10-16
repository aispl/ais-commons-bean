package pl.ais.commons.bean.validation.listener;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;
import pl.ais.commons.bean.domain.model.Activity;
import pl.ais.commons.bean.domain.model.Person;
import pl.ais.commons.bean.validation.ValidationContext;
import pl.ais.commons.bean.validation.event.ValidationListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static pl.ais.commons.bean.domain.model.Activity.anActivity;
import static pl.ais.commons.bean.validation.Constraints.notBlank;
import static pl.ais.commons.bean.validation.Constraints.required;
import static pl.ais.commons.bean.validation.ValidationContext.validationOf;

/**
 * Verifies Spring Validation Listener (SVL) expectations.
 *
 * @author Warlock, AIS.PL
 * @see SpringValidationListener Spring Validation Listener (SVL)
 * @since 1.2.1
 */
public class SpringValidationListenerExpectations {

    private static Consumer<ValidationContext<Activity>> activityIsValid() {
        return validateThat -> {
            final Activity subject = validateThat.subject();

            validateThat.valueOf(subject.getName())
                        .satisfies(required());
            validateThat.valueOf(subject.getStart())
                        .satisfies(required());
        };
    }

    @Test
    public void shouldReportInvalidMapElementAsFieldError() {
        final Person person = new Person();

        final Map<String, Activity> activities = new HashMap<>();
        Activity activity = anActivity().named("Running")
                                        .started(LocalDateTime.now())
                                        .get();
        activities.put(activity.getName(), activity);

        activity = anActivity().named("Walking").get();
        activities.put(activity.getName(), activity);

        person.setActivitiesMap(activities);

        final Errors errors = Mockito.mock(Errors.class);
        final ValidationListener listener = new SpringValidationListener(errors);

        try (final ValidationContext<Person> validateThat = validationOf(person).observedBy(listener)) {
            final Person subject = validateThat.subject();

            validateThat.forEach(subject.getActivitiesMap(), activityIsValid());
        }

        verify(errors, times(1)).rejectValue("activitiesMap['Walking'].start", "required", new Object[0], null);
        Mockito.verifyNoMoreInteractions(errors);
    }

    @Test
    public void shouldReportInvalidCollectionElementAsFieldError() {
        final Person person = new Person();

        final List<Activity> activities = new ArrayList<>();
        Activity activity = anActivity().named("Running")
                                        .started(LocalDateTime.now())
                                        .get();
        activities.add(activity);

        activity = anActivity().started(LocalDateTime.now())
                               .get();
        activities.add(activity);

        person.setActivities(activities);

        final Errors errors = Mockito.mock(Errors.class);
        final ValidationListener listener = new SpringValidationListener(errors);

        try (final ValidationContext<Person> validateThat = validationOf(person).observedBy(listener)) {
            final Person subject = validateThat.subject();

            validateThat.forEach(subject.getActivities(), activityIsValid());
        }

        verify(errors, times(1)).rejectValue("activities[1].name", "required", new Object[0], null);
        Mockito.verifyNoMoreInteractions(errors);
    }

    /**
     * Verifies if constraint violated by collection of properties' values is reported as global error.
     */
    @SuppressWarnings("unchecked")
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

        verify(errors, times(1)).reject("notBlank", new Object[0], "Both name and notes should be defined!");
        Mockito.verifyNoMoreInteractions(errors);
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

        verify(errors, times(1)).rejectValue("notes", "notBlank", new Object[0], "Notes should be defined!");
        Mockito.verifyNoMoreInteractions(errors);
    }

}
