package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateScheduleTemplateRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class CreateScheduleTemplateRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String aircraftId;

  private String departureAirportId;

  private String arrivalAirportId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  private LocalTime departureTime;

  private LocalTime arrivalTime;

  private Integer price;

  private CreateScheduleTemplateRequestDaysOfWeek daysOfWeek;

  public CreateScheduleTemplateRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public CreateScheduleTemplateRequest(String aircraftId, String departureAirportId, String arrivalAirportId, LocalDate startDate, LocalDate endDate, LocalTime departureTime, LocalTime arrivalTime, Integer price, CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
    this.aircraftId = aircraftId;
    this.departureAirportId = departureAirportId;
    this.arrivalAirportId = arrivalAirportId;
    this.startDate = startDate;
    this.endDate = endDate;
    this.departureTime = departureTime;
    this.arrivalTime = arrivalTime;
    this.price = price;
    this.daysOfWeek = daysOfWeek;
  }

  public CreateScheduleTemplateRequest aircraftId(String aircraftId) {
    this.aircraftId = aircraftId;
    return this;
  }

  /**
   * Get aircraftId
   * @return aircraftId
  */
  @NotNull 
  @Schema(name = "aircraftId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("aircraftId")
  public String getAircraftId() {
    return aircraftId;
  }

  public void setAircraftId(String aircraftId) {
    this.aircraftId = aircraftId;
  }

  public CreateScheduleTemplateRequest departureAirportId(String departureAirportId) {
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

  public CreateScheduleTemplateRequest arrivalAirportId(String arrivalAirportId) {
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

  public CreateScheduleTemplateRequest startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
  */
  @NotNull @Valid 
  @Schema(name = "startDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startDate")
  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public CreateScheduleTemplateRequest endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
  */
  @NotNull @Valid 
  @Schema(name = "endDate", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endDate")
  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public CreateScheduleTemplateRequest departureTime(LocalTime departureTime) {
    this.departureTime = departureTime;
    return this;
  }

  /**
   * Get departureTime
   * @return departureTime
  */
  @NotNull @Valid 
  @Schema(name = "departureTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("departureTime")
  public LocalTime getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(LocalTime departureTime) {
    this.departureTime = departureTime;
  }

  public CreateScheduleTemplateRequest arrivalTime(LocalTime arrivalTime) {
    this.arrivalTime = arrivalTime;
    return this;
  }

  /**
   * Get arrivalTime
   * @return arrivalTime
  */
  @NotNull @Valid 
  @Schema(name = "arrivalTime", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("arrivalTime")
  public LocalTime getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public CreateScheduleTemplateRequest price(Integer price) {
    this.price = price;
    return this;
  }

  /**
   * Get price
   * @return price
  */
  @NotNull 
  @Schema(name = "price", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("price")
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public CreateScheduleTemplateRequest daysOfWeek(CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
    return this;
  }

  /**
   * Get daysOfWeek
   * @return daysOfWeek
  */
  @NotNull @Valid 
  @Schema(name = "daysOfWeek", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("daysOfWeek")
  public CreateScheduleTemplateRequestDaysOfWeek getDaysOfWeek() {
    return daysOfWeek;
  }

  public void setDaysOfWeek(CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateScheduleTemplateRequest createScheduleTemplateRequest = (CreateScheduleTemplateRequest) o;
    return Objects.equals(this.aircraftId, createScheduleTemplateRequest.aircraftId) &&
        Objects.equals(this.departureAirportId, createScheduleTemplateRequest.departureAirportId) &&
        Objects.equals(this.arrivalAirportId, createScheduleTemplateRequest.arrivalAirportId) &&
        Objects.equals(this.startDate, createScheduleTemplateRequest.startDate) &&
        Objects.equals(this.endDate, createScheduleTemplateRequest.endDate) &&
        Objects.equals(this.departureTime, createScheduleTemplateRequest.departureTime) &&
        Objects.equals(this.arrivalTime, createScheduleTemplateRequest.arrivalTime) &&
        Objects.equals(this.price, createScheduleTemplateRequest.price) &&
        Objects.equals(this.daysOfWeek, createScheduleTemplateRequest.daysOfWeek);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aircraftId, departureAirportId, arrivalAirportId, startDate, endDate, departureTime, arrivalTime, price, daysOfWeek);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateScheduleTemplateRequest {\n");
    sb.append("    aircraftId: ").append(toIndentedString(aircraftId)).append("\n");
    sb.append("    departureAirportId: ").append(toIndentedString(departureAirportId)).append("\n");
    sb.append("    arrivalAirportId: ").append(toIndentedString(arrivalAirportId)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    departureTime: ").append(toIndentedString(departureTime)).append("\n");
    sb.append("    arrivalTime: ").append(toIndentedString(arrivalTime)).append("\n");
    sb.append("    price: ").append(toIndentedString(price)).append("\n");
    sb.append("    daysOfWeek: ").append(toIndentedString(daysOfWeek)).append("\n");
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

