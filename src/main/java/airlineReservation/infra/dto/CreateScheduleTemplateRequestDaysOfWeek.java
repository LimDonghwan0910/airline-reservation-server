package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CreateScheduleTemplateRequestDaysOfWeek
 */

@JsonTypeName("CreateScheduleTemplateRequest_daysOfWeek")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class CreateScheduleTemplateRequestDaysOfWeek implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean mon;

  private Boolean tue;

  private Boolean wed;

  private Boolean thu;

  private Boolean fri;

  private Boolean sat;

  private Boolean sun;

  public CreateScheduleTemplateRequestDaysOfWeek mon(Boolean mon) {
    this.mon = mon;
    return this;
  }

  /**
   * Get mon
   * @return mon
  */
  
  @Schema(name = "mon", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("mon")
  public Boolean getMon() {
    return mon;
  }

  public void setMon(Boolean mon) {
    this.mon = mon;
  }

  public CreateScheduleTemplateRequestDaysOfWeek tue(Boolean tue) {
    this.tue = tue;
    return this;
  }

  /**
   * Get tue
   * @return tue
  */
  
  @Schema(name = "tue", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("tue")
  public Boolean getTue() {
    return tue;
  }

  public void setTue(Boolean tue) {
    this.tue = tue;
  }

  public CreateScheduleTemplateRequestDaysOfWeek wed(Boolean wed) {
    this.wed = wed;
    return this;
  }

  /**
   * Get wed
   * @return wed
  */
  
  @Schema(name = "wed", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("wed")
  public Boolean getWed() {
    return wed;
  }

  public void setWed(Boolean wed) {
    this.wed = wed;
  }

  public CreateScheduleTemplateRequestDaysOfWeek thu(Boolean thu) {
    this.thu = thu;
    return this;
  }

  /**
   * Get thu
   * @return thu
  */
  
  @Schema(name = "thu", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thu")
  public Boolean getThu() {
    return thu;
  }

  public void setThu(Boolean thu) {
    this.thu = thu;
  }

  public CreateScheduleTemplateRequestDaysOfWeek fri(Boolean fri) {
    this.fri = fri;
    return this;
  }

  /**
   * Get fri
   * @return fri
  */
  
  @Schema(name = "fri", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("fri")
  public Boolean getFri() {
    return fri;
  }

  public void setFri(Boolean fri) {
    this.fri = fri;
  }

  public CreateScheduleTemplateRequestDaysOfWeek sat(Boolean sat) {
    this.sat = sat;
    return this;
  }

  /**
   * Get sat
   * @return sat
  */
  
  @Schema(name = "sat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sat")
  public Boolean getSat() {
    return sat;
  }

  public void setSat(Boolean sat) {
    this.sat = sat;
  }

  public CreateScheduleTemplateRequestDaysOfWeek sun(Boolean sun) {
    this.sun = sun;
    return this;
  }

  /**
   * Get sun
   * @return sun
  */
  
  @Schema(name = "sun", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("sun")
  public Boolean getSun() {
    return sun;
  }

  public void setSun(Boolean sun) {
    this.sun = sun;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateScheduleTemplateRequestDaysOfWeek createScheduleTemplateRequestDaysOfWeek = (CreateScheduleTemplateRequestDaysOfWeek) o;
    return Objects.equals(this.mon, createScheduleTemplateRequestDaysOfWeek.mon) &&
        Objects.equals(this.tue, createScheduleTemplateRequestDaysOfWeek.tue) &&
        Objects.equals(this.wed, createScheduleTemplateRequestDaysOfWeek.wed) &&
        Objects.equals(this.thu, createScheduleTemplateRequestDaysOfWeek.thu) &&
        Objects.equals(this.fri, createScheduleTemplateRequestDaysOfWeek.fri) &&
        Objects.equals(this.sat, createScheduleTemplateRequestDaysOfWeek.sat) &&
        Objects.equals(this.sun, createScheduleTemplateRequestDaysOfWeek.sun);
  }

  @Override
  public int hashCode() {
    return Objects.hash(mon, tue, wed, thu, fri, sat, sun);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateScheduleTemplateRequestDaysOfWeek {\n");
    sb.append("    mon: ").append(toIndentedString(mon)).append("\n");
    sb.append("    tue: ").append(toIndentedString(tue)).append("\n");
    sb.append("    wed: ").append(toIndentedString(wed)).append("\n");
    sb.append("    thu: ").append(toIndentedString(thu)).append("\n");
    sb.append("    fri: ").append(toIndentedString(fri)).append("\n");
    sb.append("    sat: ").append(toIndentedString(sat)).append("\n");
    sb.append("    sun: ").append(toIndentedString(sun)).append("\n");
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

