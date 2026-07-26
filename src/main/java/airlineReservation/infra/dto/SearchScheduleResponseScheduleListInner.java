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
 * SearchScheduleResponseScheduleListInner
 */

@JsonTypeName("SearchScheduleResponse_scheduleList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:00:28.871973+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchScheduleResponseScheduleListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer scheduleId;

  private Integer templateId;

  private String aircraftId;

  private String departureAirportId;

  private String arrivalAirportId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime departureDatetime;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime arrivalDatetime;

  private String status;

  public SearchScheduleResponseScheduleListInner scheduleId(Integer scheduleId) {
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

  public SearchScheduleResponseScheduleListInner templateId(Integer templateId) {
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

  public SearchScheduleResponseScheduleListInner aircraftId(String aircraftId) {
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

  public SearchScheduleResponseScheduleListInner departureAirportId(String departureAirportId) {
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

  public SearchScheduleResponseScheduleListInner arrivalAirportId(String arrivalAirportId) {
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

  public SearchScheduleResponseScheduleListInner departureDatetime(LocalDateTime departureDatetime) {
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

  public SearchScheduleResponseScheduleListInner arrivalDatetime(LocalDateTime arrivalDatetime) {
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

  public SearchScheduleResponseScheduleListInner status(String status) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchScheduleResponseScheduleListInner searchScheduleResponseScheduleListInner = (SearchScheduleResponseScheduleListInner) o;
    return Objects.equals(this.scheduleId, searchScheduleResponseScheduleListInner.scheduleId) &&
        Objects.equals(this.templateId, searchScheduleResponseScheduleListInner.templateId) &&
        Objects.equals(this.aircraftId, searchScheduleResponseScheduleListInner.aircraftId) &&
        Objects.equals(this.departureAirportId, searchScheduleResponseScheduleListInner.departureAirportId) &&
        Objects.equals(this.arrivalAirportId, searchScheduleResponseScheduleListInner.arrivalAirportId) &&
        Objects.equals(this.departureDatetime, searchScheduleResponseScheduleListInner.departureDatetime) &&
        Objects.equals(this.arrivalDatetime, searchScheduleResponseScheduleListInner.arrivalDatetime) &&
        Objects.equals(this.status, searchScheduleResponseScheduleListInner.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleId, templateId, aircraftId, departureAirportId, arrivalAirportId, departureDatetime, arrivalDatetime, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchScheduleResponseScheduleListInner {\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    templateId: ").append(toIndentedString(templateId)).append("\n");
    sb.append("    aircraftId: ").append(toIndentedString(aircraftId)).append("\n");
    sb.append("    departureAirportId: ").append(toIndentedString(departureAirportId)).append("\n");
    sb.append("    arrivalAirportId: ").append(toIndentedString(arrivalAirportId)).append("\n");
    sb.append("    departureDatetime: ").append(toIndentedString(departureDatetime)).append("\n");
    sb.append("    arrivalDatetime: ").append(toIndentedString(arrivalDatetime)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

