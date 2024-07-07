package subway;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private LineRepository lineRepository;
    private StationRepository stationRepository;

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
}
