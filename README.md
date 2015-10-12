# AIS.PL Commons - Bean

This library provides set of components and abstractions usable for bean proxying and validation.

## Usage examples

### Reporting validation errors to [Spring Framework](http://projects.spring.io/spring-framework/)

Below you may find simple example of Person bean validation, reporting validation errors to [Spring Framework](http://projects.spring.io/spring-framework/).
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
Note, that the above code will result in calling: 
```java
errors.rejectValue("notes", "notBlank", new Object[0], "Notes should be defined!");
```
each time if 'notes' property of Person bean is blank (empty or null). Path to validated property will be computed 
on-the-fly by the library classes, dropping necessity of hardcoding it into your code. Used *message code* depends
on the constraint applied. There is also possibility of defining default message used, and the message parameters.
For the details see [Constraint API](src/main/java/pl/ais/commons/bean/validation/Constraint.java).
 

