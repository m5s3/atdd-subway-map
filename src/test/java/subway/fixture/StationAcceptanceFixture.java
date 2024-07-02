package subway.fixture;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;

public class StationAcceptanceFixture {

    protected List<ExtractableResponse<Response>> stations;

    @BeforeEach
    void setUP() {
        stations = createStations();
    }

    private List<ExtractableResponse<Response>> createStations() {
        Map<String, String> stationParamsOfSookmyungEntranceStation = createStationParams("지하철역");
        Map<String, String> stationParamsOfSeoulStation = createStationParams("새로운지하철역");
        ExtractableResponse<Response> responseOfSookmyungEntraceStation = requestCreateStation(
                stationParamsOfSookmyungEntranceStation);
        ExtractableResponse<Response> responseOfSeoulStation = requestCreateStation(stationParamsOfSeoulStation);
        return List.of(responseOfSookmyungEntraceStation, responseOfSeoulStation);
    }

    private Map<String, String> createStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }

    private ExtractableResponse<Response> requestCreateStation(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }
}
