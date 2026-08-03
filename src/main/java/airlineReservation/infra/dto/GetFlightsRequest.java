package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetFlightsRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetFlightsRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String departureAirportId;

  private String arrivalAirportId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate departureDate;

  private Integer passengerCount;

  public GetFlightsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetFlightsRequest(String departureAirportId, String arrivalAirportId, LocalDate departureDate, Integer passengerCount) {
    this.departureAirportId = departureAirportId;
    this.arrivalAirportId = arrivalAirportId;
    this.departureDate = departureDate;
    this.passengerCount = passengerCount;
  }

  public GetFlightsRequest departureAirportId(String departureAirportId) {
    this.departureAirportId = departureAirportId;
    return this;
  }

  /**
   * Get departureAirportId
   * @return departureAirportId
  */
  @NotNull 
  @Schema(name = "departureAirportId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("departureAirportId")
  public String getDepartureAirportId() {
    return departureAirportId;
  }

  public void setDepartureAirportId(String departureAirportId) {
    this.departureAirportId = departureAirportId;
  }

  public GetFlightsRequest arrivalAirportId(String arrivalAirportId) {
    this.arrivalAirportId = arrivalAirportId;
    return this;
  }

  /**
   * Get arrivalAirportId
   * @return arrivalAirportId
  */
  @NotNull 
  @Schema(name = "arrivalAirportId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("arrivalAirportId")
  public String getArrivalAirportId() {
    return arrivalAirportId;
  }

  public void setArrivalAirportId(String arrivalAirportId) {
    this.arrivalAirportId = arrivalAirportId;
  }

  public GetFlightsRequest departureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
    return this;
  }

  /**
   * Get departureDate
   * @return departureDate
  */
  @NotNull @Valid 
  @Schema(name = "departureDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("departureDate")
  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public GetFlightsRequest passengerCount(Integer passengerCount) {
    this.passengerCount = passengerCount;
    return this;
  }

  /**
   * Get passengerCount
   * @return passengerCount
  */
  @NotNull 
  @Schema(name = "passengerCount", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("passengerCount")
  public Integer getPassengerCount() {
    return passengerCount;
  }

  public void setPassengerCount(Integer passengerCount) {
    this.passengerCount = passengerCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFlightsRequest getFlightsRequest = (GetFlightsRequest) o;
    return Objects.equals(this.departureAirportId, getFlightsRequest.departureAirportId) &&
        Objects.equals(this.arrivalAirportId, getFlightsRequest.arrivalAirportId) &&
        Objects.equals(this.departureDate, getFlightsRequest.departureDate) &&
        Objects.equals(this.passengerCount, getFlightsRequest.passengerCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(departureAirportId, arrivalAirportId, departureDate, passengerCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFlightsRequest {\n");
    sb.append("    departureAirportId: ").append(toIndentedString(departureAirportId)).append("\n");
    sb.append("    arrivalAirportId: ").append(toIndentedString(arrivalAirportId)).append("\n");
    sb.append("    departureDate: ").append(toIndentedString(departureDate)).append("\n");
    sb.append("    passengerCount: ").append(toIndentedString(passengerCount)).append("\n");
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

