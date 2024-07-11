package subway.Station.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import subway.Line.domain.Line;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Map<Long, Station> toMap() {
        return this.stations.stream()
                .collect(Collectors.toMap(Station::getId, station -> station));
    }

    public Station findById(Long id) {
        return stations.stream().filter(station -> station.getId().equals(id)).findAny()
                .orElseThrow(() -> new IllegalArgumentException("지하철역의 해당 ID 를 찾을수 없습니다. id=" + id));
    }
}
