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
 * SearchBookingByAdminRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchBookingByAdminRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer userId;

  private Integer aircraftId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate departureDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate arrivalDate;

  public SearchBookingByAdminRequest userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  
  @Schema(name = "user_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user_id")
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public SearchBookingByAdminRequest aircraftId(Integer aircraftId) {
    this.aircraftId = aircraftId;
    return this;
  }

  /**
   * Get aircraftId
   * @return aircraftId
  */
  
  @Schema(name = "aircraftId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftId")
  public Integer getAircraftId() {
    return aircraftId;
  }

  public void setAircraftId(Integer aircraftId) {
    this.aircraftId = aircraftId;
  }

  public SearchBookingByAdminRequest departureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
    return this;
  }

  /**
   * Get departureDate
   * @return departureDate
  */
  @Valid 
  @Schema(name = "departureDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departureDate")
  public LocalDate getDepartureDate() {
    return departureDate;
  }

  public void setDepartureDate(LocalDate departureDate) {
    this.departureDate = departureDate;
  }

  public SearchBookingByAdminRequest arrivalDate(LocalDate arrivalDate) {
    this.arrivalDate = arrivalDate;
    return this;
  }

  /**
   * Get arrivalDate
   * @return arrivalDate
  */
  @Valid 
  @Schema(name = "arrivalDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arrivalDate")
  public LocalDate getArrivalDate() {
    return arrivalDate;
  }

  public void setArrivalDate(LocalDate arrivalDate) {
    this.arrivalDate = arrivalDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchBookingByAdminRequest searchBookingByAdminRequest = (SearchBookingByAdminRequest) o;
    return Objects.equals(this.userId, searchBookingByAdminRequest.userId) &&
        Objects.equals(this.aircraftId, searchBookingByAdminRequest.aircraftId) &&
        Objects.equals(this.departureDate, searchBookingByAdminRequest.departureDate) &&
        Objects.equals(this.arrivalDate, searchBookingByAdminRequest.arrivalDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, aircraftId, departureDate, arrivalDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchBookingByAdminRequest {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    aircraftId: ").append(toIndentedString(aircraftId)).append("\n");
    sb.append("    departureDate: ").append(toIndentedString(departureDate)).append("\n");
    sb.append("    arrivalDate: ").append(toIndentedString(arrivalDate)).append("\n");
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

