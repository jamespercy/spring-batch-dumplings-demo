package batch.domain;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

import static batch.domain.Status.*;

public class Dumpling {
    @Id
    private String id;

    private DumplingType type;

    private Status status;

    private CookingMethod method;

    private LocalDateTime deliveredOn;

    private LocalDateTime servedAt;

    private LocalDateTime eatenAt;

    private DumplingEater eatenBy;

    public Dumpling() {
    }

    public Dumpling(DumplingType type) {
        this.type = type;
        this.status = Frozen;
        this.deliveredOn = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format(
                "Dumpling[id=%s, type='%s', status='%s', method='%s', deliveredOn='%s', servedAt='%s', eatenBy='%s', eatenAt='%s']",
                id, type, status, method, deliveredOn, servedAt, eatenBy, eatenAt);
    }
}
