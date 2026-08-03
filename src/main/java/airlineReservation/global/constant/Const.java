package airlineReservation.global.constant;

public final class Const {

    // 객체 생성 방지
    private Const() {
        throw new UnsupportedOperationException("생성할 수 없습니다.");
    }

    // 예약 상태
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
