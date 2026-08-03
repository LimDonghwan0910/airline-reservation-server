package airlineReservation.domain.common.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetFlightVo {

    private Integer scheduleId;
    private String aircraftId;
    private String departureAirportId;
    private String arrivalAirportId;
    private LocalDateTime departureDatetime;
    private LocalDateTime arrivalDatetime;
    private Integer price;
    private String aircraftName;

}
