package airlineReservation.global.constant;

public final class Const {

    // インスタンス生成を防止
    private Const() {
        throw new UnsupportedOperationException("インスタンスを生成できません。");
    }

    // 予約ステータス
    public static class BOOKING_STATUS {
        public static final String AVAILABLE = "AVAILABLE";
        public static final String OCCUPIED = "OCCUPIED";
        public static final String COMPLETED = "COMPLETED";
        public static final String CANCELLED = "CANCELLED";
    }

    public static class USER_ROLE {
        public static final int MEMBER = 1;
        public static final int ADMIN = 2;
    }

}
