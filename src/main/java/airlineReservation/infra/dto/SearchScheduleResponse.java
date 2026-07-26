package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.SearchScheduleResponseScheduleListInner;
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
 * SearchScheduleResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:00:28.871973+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchScheduleResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid SearchScheduleResponseScheduleListInner> scheduleList;

  public SearchScheduleResponse scheduleList(List<@Valid SearchScheduleResponseScheduleListInner> scheduleList) {
    this.scheduleList = scheduleList;
    return this;
  }

  public SearchScheduleResponse addScheduleListItem(SearchScheduleResponseScheduleListInner scheduleListItem) {
    if (this.scheduleList == null) {
      this.scheduleList = new ArrayList<>();
    }
    this.scheduleList.add(scheduleListItem);
    return this;
  }

  /**
   * Get scheduleList
   * @return scheduleList
  */
  @Valid 
  @Schema(name = "scheduleList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduleList")
  public List<@Valid SearchScheduleResponseScheduleListInner> getScheduleList() {
    return scheduleList;
  }

  public void setScheduleList(List<@Valid SearchScheduleResponseScheduleListInner> scheduleList) {
    this.scheduleList = scheduleList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchScheduleResponse searchScheduleResponse = (SearchScheduleResponse) o;
    return Objects.equals(this.scheduleList, searchScheduleResponse.scheduleList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchScheduleResponse {\n");
    sb.append("    scheduleList: ").append(toIndentedString(scheduleList)).append("\n");
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

