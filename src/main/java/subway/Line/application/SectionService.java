package subway.Line.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Line.presentation.dto.SectionRequest;

@Service
@Transactional(readOnly = true)
public class SectionService {


    public void saveSection(SectionRequest sectionRequest) {
    }

    private Section toEntity() {

    }
}
