package pl.ais.commons.bean.domain.model;

import java.time.LocalDate;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.2
 */
public class Person {

    private LocalDate dateOfBirth;

    private int height;

    private String name;

    private String notes;

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
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

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
