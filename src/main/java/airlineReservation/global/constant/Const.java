package airlineReservation.global.constant;

import java.util.List;

public final class Const {

    // インスタンス生成を防止
    private Const() {
        throw new UnsupportedOperationException("インスタンスを生成できません。");
    }

    // JWT関連変数
    public static class JWT {
        public static final String CLAIM_USER_ID = "userId";
        public static final String CLAIM_ROLE = "role";
        public static final String CLAIM_USER_NAME = "userName";
        public static final String AUTHORIZATION_HEADER = "Authorization";
        public static final String BEARER_PREFIX = "Bearer ";
    }

    // ユーザー形態
    public static class USER_ROLE {
        public static final int MEMBER = 1;
        public static final int ADMIN = 2;
    }

    // 予約ステータス
    public static class BOOKING_STATUS {
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELLED = "CANCELLED";
    }

    // 運航ステータス
    public static class SCHEDULE_STATUS {
        public static final String SCHEDULED = "SCHEDULED";
        public static final String CANCELLED = "CANCELLED";
    }

    // 座席ステータス
    public static class SEAT_STATUS {
        public static final String AVAILABLE = "AVAILABLE";
        public static final String OCCUPIED = "OCCUPIED";
        public static final String CANCELLED = "CANCELLED";
    }

    // 座席レイアウト
    public static class SEAT_LAYOUT {
        public static final List<String> COLUMN_LABELS = List.of("A", "B", "C", "D", "E", "F", "G", "H", "I");
        public static final int MIN_ROWS = 1;
        public static final int MAX_ROWS = 20;
        public static final int MIN_COLS = 1;
        public static final int MAX_COLS = 9;
    }

}
