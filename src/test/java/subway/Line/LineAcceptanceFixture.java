package subway.Line;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class LineAcceptanceFixture {

    public static Map<String, Object> 신분당선_생성(Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);
        return params;
    }

    public static Map<String, Object> 분당선_생성(Long upStationId, Long downStationId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "분당선");
        params.put("color", "bg-red-500");
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 25);
        return params;
    }

    private static Map<String, String> createStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        return params;
    }
}
