package subway;

public class LineResponse {
    private String name;
    private String color;
    private StationResponse upStationResponse;
    private StationResponse downStationResponse;
    private int distance;

    public LineResponse(String name, String color, StationResponse upStationResponse,
            StationResponse downStationResponse,
            int distance) {
        this.name = name;
        this.color = color;
        this.upStationResponse = upStationResponse;
        this.downStationResponse = downStationResponse;
        this.distance = distance;
    }
}
