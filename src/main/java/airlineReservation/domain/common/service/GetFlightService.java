package airlineReservation.domain.common.service;

import airlineReservation.domain.common.serviceInput.GetFlightServiceInput;
import airlineReservation.domain.common.serviceOutput.GetFlightServiceOutput;
import airlineReservation.domain.common.vo.GetFlightVo;
import airlineReservation.global.constant.Const;
import airlineReservation.global.exception.ErrorCode;
import airlineReservation.global.exception.InvalidInputValueException;
import airlineReservation.global.exception.NotFoundException;
import airlineReservation.infra.dto.GetFlightsResponseFlightsListInner;
import airlineReservation.infra.entity.Airport;
import airlineReservation.infra.mapper.AirportMapper;
import airlineReservation.infra.mapper.customMapper.GetFlightCustomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * フライト一覧取得処理を行うサービス。
 */
@Service
@RequiredArgsConstructor
public class GetFlightService {

    private final GetFlightCustomMapper getFlightCustomMapper;
    private final AirportMapper airportMapper;

    /**
     * 出発空港・到着空港・出発日を条件にフライト一覧を取得する。
     *
     * @param input
     * @return serviceOutput
     * @throws InvalidInputValueException 入力項目が誤っている場合
     * @throws NotFoundException 空港が存在しない場合
     */
    public GetFlightServiceOutput getFlightList(GetFlightServiceInput input) {
        validateInput(input);

        LocalDateTime start = input.getDepartureDate().atStartOfDay();
        LocalDateTime end = input.getDepartureDate().plusDays(1).atStartOfDay();

        List<GetFlightVo> scheduleList = getFlightCustomMapper.selectFlightList(
                input.getDepartureAirportId(),
                input.getArrivalAirportId(),
                start,
                end,
                input.getPassengerCount(),
                Const.SCHEDULE_STATUS.CANCELLED,
                Const.SEAT_STATUS.AVAILABLE
        );

        List<GetFlightsResponseFlightsListInner> flightList = new ArrayList<>();

        for (GetFlightVo schedule : scheduleList) {
            GetFlightsResponseFlightsListInner item = new GetFlightsResponseFlightsListInner();

            item.setScheduleId(schedule.getScheduleId());
            item.setAircraftId(schedule.getAircraftId());
            item.setDepartureAirportId(schedule.getDepartureAirportId());
            item.setArrivalAirportId(schedule.getArrivalAirportId());
            item.setDepartureDatetime(schedule.getDepartureDatetime());
            item.setArrivalDatetime(schedule.getArrivalDatetime());
            item.setPrice(schedule.getPrice());
            item.setAircraftName(schedule.getAircraftName());

            flightList.add(item);
        }

        return GetFlightServiceOutput.builder()
                .flightList(flightList)
                .build();
    }

    /**
     * 入力値チェックを行う。
     *
     * @param input
     * @throws InvalidInputValueException
     * @throws NotFoundException
     */
    private void validateInput(GetFlightServiceInput input) {
        if (input.getDepartureAirportId() == null || input.getDepartureAirportId().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "出発空港を入力してください。");
        }
        if (input.getArrivalAirportId() == null || input.getArrivalAirportId().isBlank()) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "到着空港を入力してください。");
        }
        if (input.getDepartureDate() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "出発日を入力してください。");
        }
        if (input.getPassengerCount() == null) {
            throw new InvalidInputValueException(ErrorCode.INPUT_NOT_FOUND, "搭乗者数を入力してください。");
        }
        if (input.getPassengerCount() <= 0) {
            throw new InvalidInputValueException(ErrorCode.INVALID_INPUT_VALUE, "搭乗者数は1以上で入力してください。");
        }
        if (input.getDepartureAirportId().equals(input.getArrivalAirportId())) {
            throw new InvalidInputValueException(ErrorCode.INVALID_INPUT_VALUE, "出発空港と到着空港は異なる空港を選択してください。");
        }
        if (input.getDepartureDate().isBefore(LocalDate.now())) {
            throw new InvalidInputValueException(ErrorCode.INVALID_INPUT_VALUE, "出発日は本日以降の日付を入力してください。");
        }

        validateAirportExists(input.getDepartureAirportId(), "出発空港");
        validateAirportExists(input.getArrivalAirportId(), "到着空港");
    }

    private void validateAirportExists(String airportId, String label) {
        Airport airport = airportMapper.selectByPrimaryKey(airportId);
        if (airport == null || Boolean.TRUE.equals(airport.getIsDeleted())) {
            throw new NotFoundException(ErrorCode.AIRPORT_NOT_FOUND, "登録されていない" + label + "です: " + airportId);
        }
    }
}
