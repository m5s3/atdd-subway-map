package subway.Line.presentation.dto;

import java.util.List;
import subway.Station.presentation.dto.StationResponse;

public class LineResponse {

    private Long id;
    private String name;
    private String color;
    private int distance;
    private List<StationResponse> stations;

    public LineResponse(Long id, String name, String color, StationResponse upStationResponse,
            StationResponse downStationResponse, int distance) {
        this(id, name, color, distance, List.of(upStationResponse, downStationResponse));
    }

    public LineResponse(Long id, String name, String color, int distance, List<StationResponse> stations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.stations = stations;
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

    public int getDistance() {
        return distance;
    }

    public List<StationResponse> getStations() {
        return stations;
    }
}
