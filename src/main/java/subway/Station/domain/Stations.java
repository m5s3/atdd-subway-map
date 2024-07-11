package subway.Station.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stations {

    private final List<Station> stations;

    public Stations(List<Station> stations) {
        this.stations = stations;
    }

    public Map<Long, Station> toMap() {
        return this.stations.stream()
                .collect(Collectors.toMap(Station::getId, station -> station));
    }
}
