package subway;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@DisplayName("지하철 노선 관련 기능")
@Sql(scripts = {"/truncate.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class LineAcceptanceTest {
    /**
     * Given: 새로운 지하철 노선 정보를 입력하고,
     * When: 관리자가 노선을 생성하면,
     * Then: 해당 노선이 생성되고 노선 목록에 포함된다.
     */
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        // Given
        Long firstStationId = requestCreateStation("지하철역")
                                        .jsonPath()
                                        .getObject("id", Long.class);
        Long secondStationId = requestCreateStation("새로운지하철")
                                        .jsonPath()
                                        .getObject("id", Long.class);

        // When
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", firstStationId);
        params.put("downStationId", secondStationId);
        params.put("distance", 10);
        ExtractableResponse<Response> response = requestCreateLine(params);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        List<Long> stationIds = response.jsonPath().getList("stations.id", Long.class);
        System.out.println("stationIds = " + stationIds);
        assertThat(stationIds).containsExactlyInAnyOrder(firstStationId, secondStationId);
    }

    /**
     * Given: 여러 개의 지하철 노선이 등록되어 있고,
     * When: 관리자가 지하철 노선 목록을 조회하면,
     * Then: 모든 지하철 노선 목록이 반환된다.
     */
    @DisplayName("모든 지하철 노션을 목록을 조회한다.")
    @Test
    void readLines() {
        // Given
        Long firstStationId = requestCreateStation("지하철역")
                .jsonPath()
                .getObject("id", Long.class);
        Long secondStationId = requestCreateStation("새로운지하철")
                .jsonPath()
                .getObject("id", Long.class);

        Long thirdStationId = requestCreateStation("지하철역2")
                .jsonPath()
                .getObject("id", Long.class);
        Long fourthStationId = requestCreateStation("새로운지하철2")
                .jsonPath()
                .getObject("id", Long.class);

        // When
        Map<String, Object> firstParams = new HashMap<>();
        firstParams.put("name", "신분당선");
        firstParams.put("color", "bg-red-600");
        firstParams.put("upStationId", firstStationId);
        firstParams.put("downStationId", secondStationId);
        firstParams.put("distance", 10);
        Long firstLineId = requestCreateLine(firstParams).jsonPath().getObject("id", Long.class);

        Map<String, Object> secondParams = new HashMap<>();
        secondParams.put("name", "신분당선2");
        secondParams.put("color", "bg-red-700");
        secondParams.put("upStationId", thirdStationId);
        secondParams.put("downStationId", fourthStationId);
        secondParams.put("distance", 25);
        Long secondLineId = requestCreateLine(secondParams).jsonPath().getObject("id", Long.class);

        // Then
        ExtractableResponse<Response> response = RestAssured.given().log().all().when().get("/lines").then().log().all()
                .extract();
        List<Long> lineIds = response.jsonPath().getList("id", Long.class);
        assertThat(lineIds).containsExactlyInAnyOrder(firstLineId, secondLineId);

        List<List<Integer>> ids = response.jsonPath().getList("stations.id");
        ids.get(0).addAll(ids.get(1));
        List<Long> stationIds = ids.get(0).stream().map(Long::valueOf).collect(Collectors.toList());
        assertThat(stationIds).containsExactlyInAnyOrder(firstStationId, secondStationId, thirdStationId, fourthStationId);
    }

    private static ExtractableResponse<Response> requestCreateLine(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestCreateStation(String stationName) {
        return RestAssured.given().log().all()
                .body(createStationParams(stationName))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    private Map<String, String> createStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }
}
