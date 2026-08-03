package airlineReservation.global.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 항공기 좌석 배치 유틸.
 * 프론트엔드 seatLayout.js 와 동일한 규칙으로 좌석명을 생성한다.
 */
public final class SeatLayoutUtil {

    private static final String[] COLUMN_LABELS = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
    private static final int MIN_ROWS = 1;
    private static final int MAX_ROWS = 20;
    private static final int MIN_COLS = 1;
    private static final int MAX_COLS = 9;

    private SeatLayoutUtil() {
    }

    /**
     * rowCount × columnCount 기준으로 좌석명 목록 생성.
     * 예) rowCount=2, columnCount=3 → 1A, 1B, 1C, 2A, 2B, 2C
     */
    public static List<String> generateSeatNames(int rowCount, int columnCount) {
        int safeRows = clamp(rowCount, MIN_ROWS, MAX_ROWS);
        int safeCols = clamp(columnCount, MIN_COLS, MAX_COLS);

        List<String> seatNames = new ArrayList<>(safeRows * safeCols);
        for (int row = 1; row <= safeRows; row++) {
            for (int col = 0; col < safeCols; col++) {
                seatNames.add(row + COLUMN_LABELS[col]);
            }
        }
        return seatNames;
    }

    private static int clamp(int value, int min, int max) {
        if (value < min) {
            return min;
        }
        if (value > max) {
            return max;
        }
        return value;
    }
}
