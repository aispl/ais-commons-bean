package pl.ais.commons.bean.domain.model;

import java.time.LocalDateTime;
import java.util.function.Supplier;

/**
 * @author Warlock, AIS.PL
 * @since 1.2.2
 */
public class Activity {

    private final LocalDateTime end;

    private final String name;

    private final LocalDateTime start;

    Activity(final Builder builder) {
        end = builder.end;
        name = builder.name;
        start = builder.start;
    }

    public static Builder anActivity() {
        return new Builder();
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public static class Builder implements Supplier<Activity> {

        private LocalDateTime end;

        private String name;

        private LocalDateTime start;

        public Builder finished(final LocalDateTime end) {
            this.end = end;
            return this;
        }

        @Override
        public Activity get() {
            return new Activity(this);
        }

        public Builder named(final String name) {
            this.name = name;
            return this;
        }

        public Builder started(final LocalDateTime start) {
            this.start = start;
            return this;
        }

    }

}
