package subway;

import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Line() {}

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this(null, name, color, upStationId, downStationId, distance);
    }

    public Line(Long id, String name, String color, Long upStationId, Long downStationId, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public List<Long> getStationsIds() {
        return List.of(upStationId, downStationId);
    }

    public void update(LineRequest lineRequest) {
        if (!Objects.isNull(lineRequest.getName())) {
            this.name = lineRequest.getName();
        }
        if (!Objects.isNull(lineRequest.getColor())) {
            this.color = lineRequest.getColor();
        }
        if (lineRequest.getDistance() > 0) {
            this.distance = lineRequest.getDistance();
        }
        if (!Objects.isNull(lineRequest.getUpStationId())) {
            this.upStationId = lineRequest.getUpStationId();
        }

        if (!Objects.isNull(lineRequest.getDownStationId())) {
            this.downStationId = lineRequest.getDownStationId();
        }
    }
}
