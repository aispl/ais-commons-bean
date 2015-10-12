# AIS.PL Commons - Bean

This library provides set of components usable for bean proxying and validation.

## Usage examples

### Reporting validation errors to [Spring Framework](http://projects.spring.io/spring-framework/) via [Errors](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/Errors.html)

Below you may find simple example of Person bean validation, reporting validation errors to [Spring Framework](http://projects.spring.io/spring-framework/).
Note, that the code below will result in calling: *errors.rejectValue("notes", "notBlank", new Object[0], "Notes should be defined!");*
each time if 'notes' property of Person bean is blank (empty or null).
```java
import org.springframework.validation.Errors;
import pl.ais.commons.bean.domain.model.Person;
import pl.ais.commons.bean.validation.ValidationContext;
import pl.ais.commons.bean.validation.event.ValidationListener;
import pl.ais.commons.bean.validation.listener.SpringValidationListener;
...
import static pl.ais.commons.bean.validation.Constraints.notBlank;
import static pl.ais.commons.bean.validation.ValidationContext.validationOf;
...
    public void validate(final Person person, final Errors errors) {                
        final ValidationListener listener = new SpringValidationListener(errors);

        try (final ValidationContext<Person> validateThat = validationOf(person).observedBy(listener)) {

            final Person subject = validateThat.subject();

            validateThat.valueOf(subject.getNotes())
                        .satisfies(notBlank().withDescription("Notes should be defined!"));
            ...                        
        }
    }    
``` 
 

