package airlineReservation.global.util;

import java.util.ArrayList;
import java.util.List;

/**
 * 航空機座席レイアウトユーティリティ。
 * フロントエンド seatLayout.js と同じ規則で座席名を生成する。
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
     * rowCount × columnCount を基準に座席名一覧を生成する。
     * 例) rowCount=2, columnCount=3 → 1A, 1B, 1C, 2A, 2B, 2C
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
