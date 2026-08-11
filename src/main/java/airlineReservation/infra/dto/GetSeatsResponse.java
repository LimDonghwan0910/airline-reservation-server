package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.GetSeatsResponseSeatListInner;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GetSeatsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetSeatsResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer scheduleId;

  @Valid
  private List<@Valid GetSeatsResponseSeatListInner> seatList;

  public GetSeatsResponse scheduleId(Integer scheduleId) {
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

  public GetSeatsResponse seatList(List<@Valid GetSeatsResponseSeatListInner> seatList) {
    this.seatList = seatList;
    return this;
  }

  public GetSeatsResponse addSeatListItem(GetSeatsResponseSeatListInner seatListItem) {
    if (this.seatList == null) {
      this.seatList = new ArrayList<>();
    }
    this.seatList.add(seatListItem);
    return this;
  }

  /**
   * Get seatList
   * @return seatList
  */
  @Valid 
  @Schema(name = "seatList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seatList")
  public List<@Valid GetSeatsResponseSeatListInner> getSeatList() {
    return seatList;
  }

  public void setSeatList(List<@Valid GetSeatsResponseSeatListInner> seatList) {
    this.seatList = seatList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSeatsResponse getSeatsResponse = (GetSeatsResponse) o;
    return Objects.equals(this.scheduleId, getSeatsResponse.scheduleId) &&
        Objects.equals(this.seatList, getSeatsResponse.seatList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleId, seatList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSeatsResponse {\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    seatList: ").append(toIndentedString(seatList)).append("\n");
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

