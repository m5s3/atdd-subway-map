package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@Sql(scripts = {"/truncate.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강남역");

        ExtractableResponse<Response> response =
                RestAssured.given().log().all()
                        .body(params)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/stations")
                        .then().log().all()
                        .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        List<String> stationNames =
                requestStations();
        assertThat(stationNames).containsAnyOf("강남역");
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("모든 지하철역 목록을 조회한다.")
    @Test
    void readStations() {
        // Given
        requestCreateStation("숙대입구");
        requestCreateStation("서울역");

        // When
        List<String> stationsNames = requestStations();

        // Then
        assertThat(stationsNames).containsExactlyInAnyOrder("숙대입구", "서울역");
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다")
    @Test
    void deleteStation() {
        // Given
        ExtractableResponse<Response> responseOfSookmyungEntraceStation = requestCreateStation("숙대입구");
        ExtractableResponse<Response> responseOfSeoulStation = requestCreateStation("서울역");
        List<ExtractableResponse<Response>> stations = List.of(responseOfSookmyungEntraceStation, responseOfSeoulStation);
        ExtractableResponse<Response> responseExtractableResponse = stations.get(0);
        Long stationId = responseExtractableResponse.jsonPath().getObject("id", Long.class);
        String stationName = responseExtractableResponse.jsonPath().getObject("name", String.class);

        // When
        RestAssured.given().log().all()
                .when()
                .delete("/stations/" + stationId)
                .then().log().all();

        // Then
        List<String> stationsNames = requestStations();
        assertThat(stationsNames).doesNotContain(stationName);
    }

    private static List<String> requestStations() {
        return RestAssured.given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract().jsonPath().getList("name", String.class);
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