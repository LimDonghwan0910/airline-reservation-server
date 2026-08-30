package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetSeatsRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-30T17:47:13.211020+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetSeatsRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer scheduleId;

  public GetSeatsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public GetSeatsRequest(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  public GetSeatsRequest scheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
    return this;
  }

  /**
   * Get scheduleId
   * @return scheduleId
  */
  @NotNull 
  @Schema(name = "scheduleId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("scheduleId")
  public Integer getScheduleId() {
    return scheduleId;
  }

  public void setScheduleId(Integer scheduleId) {
    this.scheduleId = scheduleId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSeatsRequest getSeatsRequest = (GetSeatsRequest) o;
    return Objects.equals(this.scheduleId, getSeatsRequest.scheduleId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSeatsRequest {\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
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

