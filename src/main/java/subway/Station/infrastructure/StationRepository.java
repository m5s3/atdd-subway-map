package subway.Station.infrastructure;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import subway.Station.domain.Station;

public interface StationRepository extends JpaRepository<Station, Long> {

    List<Station> findStationsByIdIn(List<Long> ids);
}