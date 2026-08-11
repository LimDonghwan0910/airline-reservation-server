package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.CreateScheduleTemplateRequestDaysOfWeek;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
 * SearchScheduleTemplateResponseScheduleTemplateListInner
 */

@JsonTypeName("SearchScheduleTemplateResponse_scheduleTemplateList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchScheduleTemplateResponseScheduleTemplateListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer templateId;

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

  public SearchScheduleTemplateResponseScheduleTemplateListInner templateId(Integer templateId) {
    this.templateId = templateId;
    return this;
  }

  /**
   * Get templateId
   * @return templateId
  */
  
  @Schema(name = "templateId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("templateId")
  public Integer getTemplateId() {
    return templateId;
  }

  public void setTemplateId(Integer templateId) {
    this.templateId = templateId;
  }

  public SearchScheduleTemplateResponseScheduleTemplateListInner aircraftId(String aircraftId) {
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

  public SearchScheduleTemplateResponseScheduleTemplateListInner departureAirportId(String departureAirportId) {
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

  public SearchScheduleTemplateResponseScheduleTemplateListInner arrivalAirportId(String arrivalAirportId) {
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

  public SearchScheduleTemplateResponseScheduleTemplateListInner startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  /**
   * Get startDate
   * @return startDate
  */
  @Valid 
  @Schema(name = "startDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("startDate")
  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public SearchScheduleTemplateResponseScheduleTemplateListInner endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  /**
   * Get endDate
   * @return endDate
  */
  @Valid 
  @Schema(name = "endDate", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("endDate")
  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public SearchScheduleTemplateResponseScheduleTemplateListInner departureTime(LocalTime departureTime) {
    this.departureTime = departureTime;
    return this;
  }

  /**
   * Get departureTime
   * @return departureTime
  */
  @Valid 
  @Schema(name = "departureTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("departureTime")
  public LocalTime getDepartureTime() {
    return departureTime;
  }

  public void setDepartureTime(LocalTime departureTime) {
    this.departureTime = departureTime;
  }

  public SearchScheduleTemplateResponseScheduleTemplateListInner arrivalTime(LocalTime arrivalTime) {
    this.arrivalTime = arrivalTime;
    return this;
  }

  /**
   * Get arrivalTime
   * @return arrivalTime
  */
  @Valid 
  @Schema(name = "arrivalTime", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("arrivalTime")
  public LocalTime getArrivalTime() {
    return arrivalTime;
  }

  public void setArrivalTime(LocalTime arrivalTime) {
    this.arrivalTime = arrivalTime;
  }

  public SearchScheduleTemplateResponseScheduleTemplateListInner price(Integer price) {
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

  public SearchScheduleTemplateResponseScheduleTemplateListInner daysOfWeek(CreateScheduleTemplateRequestDaysOfWeek daysOfWeek) {
    this.daysOfWeek = daysOfWeek;
    return this;
  }

  /**
   * Get daysOfWeek
   * @return daysOfWeek
  */
  @Valid 
  @Schema(name = "daysOfWeek", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    SearchScheduleTemplateResponseScheduleTemplateListInner searchScheduleTemplateResponseScheduleTemplateListInner = (SearchScheduleTemplateResponseScheduleTemplateListInner) o;
    return Objects.equals(this.templateId, searchScheduleTemplateResponseScheduleTemplateListInner.templateId) &&
        Objects.equals(this.aircraftId, searchScheduleTemplateResponseScheduleTemplateListInner.aircraftId) &&
        Objects.equals(this.departureAirportId, searchScheduleTemplateResponseScheduleTemplateListInner.departureAirportId) &&
        Objects.equals(this.arrivalAirportId, searchScheduleTemplateResponseScheduleTemplateListInner.arrivalAirportId) &&
        Objects.equals(this.startDate, searchScheduleTemplateResponseScheduleTemplateListInner.startDate) &&
        Objects.equals(this.endDate, searchScheduleTemplateResponseScheduleTemplateListInner.endDate) &&
        Objects.equals(this.departureTime, searchScheduleTemplateResponseScheduleTemplateListInner.departureTime) &&
        Objects.equals(this.arrivalTime, searchScheduleTemplateResponseScheduleTemplateListInner.arrivalTime) &&
        Objects.equals(this.price, searchScheduleTemplateResponseScheduleTemplateListInner.price) &&
        Objects.equals(this.daysOfWeek, searchScheduleTemplateResponseScheduleTemplateListInner.daysOfWeek);
  }

  @Override
  public int hashCode() {
    return Objects.hash(templateId, aircraftId, departureAirportId, arrivalAirportId, startDate, endDate, departureTime, arrivalTime, price, daysOfWeek);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchScheduleTemplateResponseScheduleTemplateListInner {\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
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

