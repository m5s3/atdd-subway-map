package subway.Line.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

    @OneToMany(
            mappedBy = "line",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Section> sections = new ArrayList<>();

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
        System.out.println("downStationId = " + downStationId);
        System.out.println("upStationId = " + upStationId);
        this.addSection(upStationId, downStationId, distance);
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

    public void updateName(String name) {
        this.name = name;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void addSection(Long upStationId, Long downStationId, int distance) {
        if (!this.sections.isEmpty() &&
                !this.downStationId.equals(upStationId)) {
            throw new IllegalArgumentException("기존 구간의 하행 종점역이 새로운 구간 상행역이 되어야 합니다.");
        }

        if (!this.sections.isEmpty() &&
                this.downStationId.equals(downStationId)) {
            throw new IllegalArgumentException("새로운 구간의 하행 종점역과 기존 구간의 하행 종점역은 같으면 안됩니다.");
        }
        this.sections.add(new Section(upStationId, downStationId, distance, this));
        this.distance = calculateDistance();
        System.out.println("this.distance = " + this.distance);
    }

    private int calculateDistance() {
        System.out.println("this.sections = " + this.sections);
        return this.sections.stream()
                .mapToInt(Section::getDistance).sum();
    }
}
