package pl.ais.commons.bean.domain.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.2
 */
public class Person {

    private List<Activity> activities;

    private Map<String, Integer> colleagues;

    private LocalDate dateOfBirth;

    private Map<Integer, String> favorites;

    private int height;

    private String name;

    private String notes;

    public List<Activity> getActivities() {
        return activities;
    }

    public Map<String, Integer> getColleagues() {
        return colleagues;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Map<Integer, String> getFavorites() {
        return favorites;
    }

    public int getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public void setActivities(final List<Activity> activities) {
        this.activities = activities;
    }

    public void setColleagues(final Map<String, Integer> colleagues) {
        this.colleagues = colleagues;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setFavorites(final Map<Integer, String> favorites) {
        this.favorites = favorites;
    }

    public void setHeight(final int height) {
        this.height = height;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setNotes(final String notes) {
        this.notes = notes;
    }

}
