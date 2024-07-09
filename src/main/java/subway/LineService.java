package subway;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(toEntity(lineRequest));
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        Station upStation = stationRepository.findById(line.getUpStationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지하철 역은 존재하지 않습니다. id=" + line.getUpStationId()));

        Station downStation = stationRepository.findById(line.getDownStationId())
                .orElseThrow(() -> new IllegalArgumentException("해당 지하철 역은 존재하지 않습니다. id=" + line.getDownStationId()));

        return new LineResponse(line.getId(), line.getName(), line.getColor(),
                StationResponse.fromEntity(upStation), StationResponse.fromEntity(downStation), line.getDistance());
    }

    private Line toEntity(LineRequest lineRequest) {
        return new Line(
                lineRequest.getName(),
                lineRequest.getColor(),
                lineRequest.getUpStationId(),
                lineRequest.getDownStationId(),
                lineRequest.getDistance());
    }

    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        Map<Long, Station> stations = fetchStations(fetchStationsIds(lines));
        return lines.stream().map(line ->
                        new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(),
                                createStationsResponses(stations, line))
                )
                .collect(Collectors.toList());
    }

    public LineResponse findLine(Long lineId) {
        Line line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("해당 지하철 노선은 존재하지 않습니다. id=" + lineId));
        Map<Long, Station> stations = fetchStations(fetchStationsIds(List.of(line)));
        return new LineResponse(line.getId(), line.getName(), line.getColor(), line.getDistance(), createStationsResponses(stations, line));
    }

    private List<StationResponse> createStationsResponses(Map<Long, Station> stations, Line line) {
        Station upStation = stations.get(line.getUpStationId());
        Station downStation = stations.get(line.getDownStationId());
        return List.of(StationResponse.fromEntity(upStation),
                StationResponse.fromEntity(downStation));
    }

    private List<Long> fetchStationsIds(List<Line> lines) {
        List<Long> ids = new ArrayList<>();
        lines.forEach(line -> ids.addAll(line.getStationsIds()));
        return ids.stream().distinct().collect(Collectors.toList());
    }

    private Map<Long, Station> fetchStations(List<Long> ids) {
        return new Stations(stationRepository.findStationsByIdIn(ids)).toMap();
    }
}
