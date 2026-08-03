package airlineReservation.global.constant;

public class ApiEndpoints {
    // API 버전 관리용 베이스 프리픽스
    public static final String API_V1 = "/api/v1";
    public static final String ADMIN_V1 = "/api/v1/admin";

    // 유저
    public static class Auth {
        public static final String BASE = API_V1 + "/auth";
        public static final String SIGNUP = "/signup";
        public static final String LOGIN = "/login";
    }

    // 공통
    public static class Common {
        public static final String GET_AIRPORTS = API_V1 + "/airports/get";
        public static final String GET_FLIGHTS = API_V1 + "/flights/get";
        public static final String GET_SEATS = API_V1 + "/seats/get";
    }

    // 일정
    public static class Schedule {
        public static final String BASE = ADMIN_V1 + "/schedules";
        public static final String BY_ID = BASE + "/{scheduleId}";
    }

    // 예약
    public static class Booking {
        public static final String CREATE_BOOKING = API_V1 + "/bookings/create";
        public static final String SEARCH_BOOKING = API_V1 + "/bookings/search";
        public static final String DELETE_BOOKING = API_V1 + "/bookings/delete";
    }

    // 관리자
    public static class Admin {
        public static final String CREATE_AIRCRAFT = ADMIN_V1 + "/aircrafts/create";
        public static final String SEARCH_AIRCRAFT = ADMIN_V1 + "/aircrafts/search";
        public static final String UPDATE_AIRCRAFT = ADMIN_V1 + "/aircrafts/update";
        public static final String DELETE_AIRCRAFT = ADMIN_V1 + "/aircrafts/delete";
        public static final String CREATE_SCHEDULE_TEMPLATE = ADMIN_V1 + "/schedule-template/create";
        public static final String SEARCH_SCHEDULE_TEMPLATE = ADMIN_V1 + "/schedule-template/search";
        public static final String DELETE_SCHEDULE_TEMPLATE = ADMIN_V1 + "/schedule-template/delete";
        public static final String SEARCH_SCHEDULE = ADMIN_V1 + "/schedule/search";
        public static final String DELETE_SCHEDULE = ADMIN_V1 + "/schedule/delete";
    }
}
