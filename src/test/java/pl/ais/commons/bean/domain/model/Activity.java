package pl.ais.commons.bean.domain.model;

import java.time.LocalDateTime;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.2
 */
public class Activity {

    private LocalDateTime end;

    private String name;

    private LocalDateTime start;

    public LocalDateTime getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setEnd(final LocalDateTime end) {
        this.end = end;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setStart(final LocalDateTime start) {
        this.start = start;
    }

}
