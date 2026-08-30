package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * SearchBookingResponseBookingListInner
 */

@JsonTypeName("SearchBookingResponse_bookingList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-30T17:47:13.211020+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchBookingResponseBookingListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer bookingId;

  private Integer userId;

  private Integer scheduleId;

  private String userName;

  private String aircraftId;

  private String departureAirportId;

  private String arrivalAirportId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime departureDatetime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime arrivalDatetime;

  private String status;

  private Integer totalPrice;

  private Integer passengerCount;

  @Valid
  private List<@Valid CreateBookingRequestPassengerListInner> seats;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime bookedTime;

  public SearchBookingResponseBookingListInner bookingId(Integer bookingId) {
    this.bookingId = bookingId;
    return this;
  }

  /**
   * Get bookingId
   * @return bookingId
  */
  
  @Schema(name = "bookingId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bookingId")
  public Integer getBookingId() {
    return bookingId;
  }

  public void setBookingId(Integer bookingId) {
    this.bookingId = bookingId;
  }

  public SearchBookingResponseBookingListInner userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  
  @Schema(name = "userId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userId")
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public SearchBookingResponseBookingListInner scheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
    return this;
  }

  /**
   * Get scheduleId
   * @return scheduleId
  */
  
  @Schema(name = "scheduleId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduleId")
  public Integer getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  public SearchBookingResponseBookingListInner userName(String userName) {
    this.userName = userName;
    return this;
  }

  /**
   * Get userName
   * @return userName
  */
  
  @Schema(name = "userName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("userName")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public SearchBookingResponseBookingListInner aircraftId(String aircraftId) {
    this.aircraftId = aircraftId;
    return this;
  }

  /**
   * Get aircraftId
   * @return aircraftId
  */
  
  @Schema(name = "aircraftId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftId")
  public String getAircraftId() {
    return aircraftId;
  }

  public void setAircraftId(String aircraftId) {
    this.aircraftId = aircraftId;
  }

  public SearchBookingResponseBookingListInner departureAirportId(String departureAirportId) {
    this.departureAirportId = departureAirportId;
    return this;
  }

  /**
   * Get departureAirportId
   * @return departureAirportId
  */
  
  @Schema(name = "departureAirportId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departureAirportId")
  public String getDepartureAirportId() {
    return departureAirportId;
  }

  public void setDepartureAirportId(String departureAirportId) {
    this.departureAirportId = departureAirportId;
  }

  public SearchBookingResponseBookingListInner arrivalAirportId(String arrivalAirportId) {
    this.arrivalAirportId = arrivalAirportId;
    return this;
  }

  /**
   * Get arrivalAirportId
   * @return arrivalAirportId
  */
  
  @Schema(name = "arrivalAirportId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arrivalAirportId")
  public String getArrivalAirportId() {
    return arrivalAirportId;
  }

  public void setArrivalAirportId(String arrivalAirportId) {
    this.arrivalAirportId = arrivalAirportId;
  }

  public SearchBookingResponseBookingListInner departureDatetime(LocalDateTime departureDatetime) {
    this.departureDatetime = departureDatetime;
    return this;
  }

  /**
   * Get departureDatetime
   * @return departureDatetime
  */
  @Valid 
  @Schema(name = "departureDatetime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departureDatetime")
  public LocalDateTime getDepartureDatetime() {
    return departureDatetime;
  }

  public void setDepartureDatetime(LocalDateTime departureDatetime) {
    this.departureDatetime = departureDatetime;
  }

  public SearchBookingResponseBookingListInner arrivalDatetime(LocalDateTime arrivalDatetime) {
    this.arrivalDatetime = arrivalDatetime;
    return this;
  }

  /**
   * Get arrivalDatetime
   * @return arrivalDatetime
  */
  @Valid 
  @Schema(name = "arrivalDatetime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arrivalDatetime")
  public LocalDateTime getArrivalDatetime() {
    return arrivalDatetime;
  }

  public void setArrivalDatetime(LocalDateTime arrivalDatetime) {
    this.arrivalDatetime = arrivalDatetime;
  }

  public SearchBookingResponseBookingListInner status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public SearchBookingResponseBookingListInner totalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  /**
   * Get totalPrice
   * @return totalPrice
  */
  
  @Schema(name = "totalPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPrice")
  public Integer getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
  }

  public SearchBookingResponseBookingListInner passengerCount(Integer passengerCount) {
    this.passengerCount = passengerCount;
    return this;
  }

  /**
   * Get passengerCount
   * @return passengerCount
  */
  
  @Schema(name = "passengerCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passengerCount")
  public Integer getPassengerCount() {
    return passengerCount;
  }

  public void setPassengerCount(Integer passengerCount) {
    this.passengerCount = passengerCount;
  }

  public SearchBookingResponseBookingListInner seats(List<@Valid CreateBookingRequestPassengerListInner> seats) {
    this.seats = seats;
    return this;
  }

  public SearchBookingResponseBookingListInner addSeatsItem(CreateBookingRequestPassengerListInner seatsItem) {
    if (this.seats == null) {
      this.seats = new ArrayList<>();
    }
    this.seats.add(seatsItem);
    return this;
  }

  /**
   * Get seats
   * @return seats
  */
  @Valid 
  @Schema(name = "seats", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seats")
  public List<@Valid CreateBookingRequestPassengerListInner> getSeats() {
    return seats;
  }

  public void setSeats(List<@Valid CreateBookingRequestPassengerListInner> seats) {
    this.seats = seats;
  }

  public SearchBookingResponseBookingListInner bookedTime(LocalDateTime bookedTime) {
    this.bookedTime = bookedTime;
    return this;
  }

  /**
   * Get bookedTime
   * @return bookedTime
  */
  @Valid 
  @Schema(name = "bookedTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bookedTime")
  public LocalDateTime getBookedTime() {
    return bookedTime;
  }

  public void setBookedTime(LocalDateTime bookedTime) {
    this.bookedTime = bookedTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchBookingResponseBookingListInner searchBookingResponseBookingListInner = (SearchBookingResponseBookingListInner) o;
    return Objects.equals(this.bookingId, searchBookingResponseBookingListInner.bookingId) &&
        Objects.equals(this.userId, searchBookingResponseBookingListInner.userId) &&
        Objects.equals(this.scheduleId, searchBookingResponseBookingListInner.scheduleId) &&
        Objects.equals(this.userName, searchBookingResponseBookingListInner.userName) &&
        Objects.equals(this.aircraftId, searchBookingResponseBookingListInner.aircraftId) &&
        Objects.equals(this.departureAirportId, searchBookingResponseBookingListInner.departureAirportId) &&
        Objects.equals(this.arrivalAirportId, searchBookingResponseBookingListInner.arrivalAirportId) &&
        Objects.equals(this.departureDatetime, searchBookingResponseBookingListInner.departureDatetime) &&
        Objects.equals(this.arrivalDatetime, searchBookingResponseBookingListInner.arrivalDatetime) &&
        Objects.equals(this.status, searchBookingResponseBookingListInner.status) &&
        Objects.equals(this.totalPrice, searchBookingResponseBookingListInner.totalPrice) &&
        Objects.equals(this.passengerCount, searchBookingResponseBookingListInner.passengerCount) &&
        Objects.equals(this.seats, searchBookingResponseBookingListInner.seats) &&
        Objects.equals(this.bookedTime, searchBookingResponseBookingListInner.bookedTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bookingId, userId, scheduleId, userName, aircraftId, departureAirportId, arrivalAirportId, departureDatetime, arrivalDatetime, status, totalPrice, passengerCount, seats, bookedTime);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchBookingResponseBookingListInner {\n");
    sb.append("    bookingId: ").append(toIndentedString(bookingId)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    userName: ").append(toIndentedString(userName)).append("\n");
    sb.append("    aircraftId: ").append(toIndentedString(aircraftId)).append("\n");
    sb.append("    departureAirportId: ").append(toIndentedString(departureAirportId)).append("\n");
    sb.append("    arrivalAirportId: ").append(toIndentedString(arrivalAirportId)).append("\n");
    sb.append("    departureDatetime: ").append(toIndentedString(departureDatetime)).append("\n");
    sb.append("    arrivalDatetime: ").append(toIndentedString(arrivalDatetime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
    sb.append("    passengerCount: ").append(toIndentedString(passengerCount)).append("\n");
    sb.append("    seats: ").append(toIndentedString(seats)).append("\n");
    sb.append("    bookedTime: ").append(toIndentedString(bookedTime)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

