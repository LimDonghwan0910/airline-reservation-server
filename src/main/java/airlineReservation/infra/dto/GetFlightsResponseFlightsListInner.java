package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetFlightsResponseFlightsListInner
 */

@JsonTypeName("GetFlightsResponse_FlightsList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetFlightsResponseFlightsListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer scheduleId;

  private String aircraftId;

  private String departureAirportId;

  private String arrivalAirportId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime departureDatetime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime arrivalDatetime;

  private Integer price;

  private String aircraftName;

  public GetFlightsResponseFlightsListInner scheduleId(Integer scheduleId) {
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

  public GetFlightsResponseFlightsListInner aircraftId(String aircraftId) {
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

  public GetFlightsResponseFlightsListInner departureAirportId(String departureAirportId) {
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

  public GetFlightsResponseFlightsListInner arrivalAirportId(String arrivalAirportId) {
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

  public GetFlightsResponseFlightsListInner departureDatetime(LocalDateTime departureDatetime) {
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

  public GetFlightsResponseFlightsListInner arrivalDatetime(LocalDateTime arrivalDatetime) {
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

  public GetFlightsResponseFlightsListInner price(Integer price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  */
  
  @Schema(name = "price", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("price")
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public GetFlightsResponseFlightsListInner aircraftName(String aircraftName) {
    this.aircraftName = aircraftName;
    return this;
  }

  /**
   * Get aircraftName
   * @return aircraftName
  */
  
  @Schema(name = "aircraftName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftName")
  public String getAircraftName() {
    return aircraftName;
  }

  public void setAircraftName(String aircraftName) {
    this.aircraftName = aircraftName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFlightsResponseFlightsListInner getFlightsResponseFlightsListInner = (GetFlightsResponseFlightsListInner) o;
    return Objects.equals(this.scheduleId, getFlightsResponseFlightsListInner.scheduleId) &&
        Objects.equals(this.aircraftId, getFlightsResponseFlightsListInner.aircraftId) &&
        Objects.equals(this.departureAirportId, getFlightsResponseFlightsListInner.departureAirportId) &&
        Objects.equals(this.arrivalAirportId, getFlightsResponseFlightsListInner.arrivalAirportId) &&
        Objects.equals(this.departureDatetime, getFlightsResponseFlightsListInner.departureDatetime) &&
        Objects.equals(this.arrivalDatetime, getFlightsResponseFlightsListInner.arrivalDatetime) &&
        Objects.equals(this.price, getFlightsResponseFlightsListInner.price) &&
        Objects.equals(this.aircraftName, getFlightsResponseFlightsListInner.aircraftName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleId, aircraftId, departureAirportId, arrivalAirportId, departureDatetime, arrivalDatetime, price, aircraftName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFlightsResponseFlightsListInner {\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    aircraftId: ").append(toIndentedString(aircraftId)).append("\n");
    sb.append("    departureAirportId: ").append(toIndentedString(departureAirportId)).append("\n");
    sb.append("    arrivalAirportId: ").append(toIndentedString(arrivalAirportId)).append("\n");
    sb.append("    departureDatetime: ").append(toIndentedString(departureDatetime)).append("\n");
    sb.append("    arrivalDatetime: ").append(toIndentedString(arrivalDatetime)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    aircraftName: ").append(toIndentedString(aircraftName)).append("\n");
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

