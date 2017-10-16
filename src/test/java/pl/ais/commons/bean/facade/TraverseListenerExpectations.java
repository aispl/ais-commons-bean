package pl.ais.commons.bean.facade;

import org.junit.Assert;
import org.junit.Test;
import pl.ais.commons.bean.domain.model.Activity;
import pl.ais.commons.bean.domain.model.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static pl.ais.commons.bean.domain.model.Activity.anActivity;

/**
 * Verifies the traverse listener expectations.
 *
 * @author Warlock, AIS.PL
 * @since 1.2.3
 */
public class TraverseListenerExpectations {

    /**
     * Verifies if the traverse listener will track simple property access.
     */
    @Test
    public void shouldTrackSimplePropertyAccess() {

        // Given bean instance, bean property traverse listener, and facade built over the bean.
        final Person person = new Person();
        final TraverseListener listener = new TraverseListener();
        final Person facade = Facade.over(person, listener);

        // When we access simple facade's property
        facade.getDateOfBirth();

        // Then the listener should provide us with an access to the property path.
        Assert.assertEquals("Traverse listener should point to the accessed property.", "dateOfBirth", listener.asPath());
    }

    /**
     * Verifies if the traverse listener will track map property access.
     */
    @Test
    public void shouldTrackMapPropertyAccess() {

        // Given bean instance, bean property traverse listener, and facade built over the bean.
        final Person person = new Person();
        final Map<Integer, String> favorites = new HashMap<>();
        favorites.put(5, "Winnie the Pooh");
        favorites.put(10, "Batman");
        favorites.put(15, "Darth Vader");
        person.setFavorites(favorites);

        final TraverseListener listener = new TraverseListener();
        final Person facade = Facade.over(person, listener);

        // When we access map entry of facade's property
        facade.getFavorites().get(15);

        // Then the listener should provide us with an access to the map entry path.
        Assert.assertEquals("Traverse listener should point to the accessed property.", "favorites[15]", listener.asPath());
    }

    /**
     * Verifies if the traverse listener will track String keyed map property access.
     */
    @Test
    public void shouldTrackStringKeyedMapPropertyAccess() {

        // Given bean instance, bean property traverse listener, and facade built over the bean.
        final Person person = new Person();
        final Map<String, Integer> colleagues = new HashMap<>();
        colleagues.put("Jack", 11);
        colleagues.put("John", 14);
        colleagues.put("Frank", 13);
        person.setColleagues(colleagues);

        final TraverseListener listener = new TraverseListener();
        final Person facade = Facade.over(person, listener);

        // When we access string-keyed map entry of facade's property
        facade.getColleagues().get("Frank");

        // Then the listener should provide us with an access to the map entry path.
        Assert.assertEquals("Traverse listener should point to the accessed property.", "colleagues['Frank']", listener.asPath());
    }

    /**
     * Verifies if the traverse listener will track list property access.
     */
    @Test
    public void shouldTrackListPropertyAccess() {

        // Given bean instance, bean property traverse listener, and facade built over the bean.
        final Person person = new Person();
        final List<Activity> activities = new ArrayList<>();
        activities.add(anActivity().get());
        activities.add(anActivity().get());
        person.setActivities(activities);

        final TraverseListener listener = new TraverseListener();
        final Person facade = Facade.over(person, listener);

        // When we access list element of facade's property
        facade.getActivities().get(1);

        // Then the listener should provide us with an access to the element path.
        Assert.assertEquals("Traverse listener should point to the accessed property.", "activities[1]", listener.asPath());
    }

    /**
     * Verifies if the traverse listener will track nested property access.
     */
    @Test
    public void shouldTrackNestedPropertyAccess() {

        // Given bean instance, bean property traverse listener, and facade built over the bean.
        final Person person = new Person();
        final List<Activity> activities = new ArrayList<>();
        activities.add(anActivity().get());
        activities.add(anActivity().get());
        person.setActivities(activities);

        final TraverseListener listener = new TraverseListener();
        final Person facade = Facade.over(person, listener);

        // When we access nested facade's property
        facade.getActivities().get(1).getStart();

        // Then the listener should provide us with an access to the nested property path.
        Assert.assertEquals("Traverse listener should point to the accessed property.", "activities[1].start", listener.asPath());
    }

}
