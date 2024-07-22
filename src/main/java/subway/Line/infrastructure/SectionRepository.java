package subway.Line.infrastructure;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import subway.Line.domain.Line;
import subway.Line.domain.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s WHERE s.line = :line")
    List<Section> findByLine(Line line);

    Optional<Section> findByLineAndDownStationId(Line line, Long downStationId);
}
