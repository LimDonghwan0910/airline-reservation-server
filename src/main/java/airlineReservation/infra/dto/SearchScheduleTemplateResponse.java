package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.SearchScheduleTemplateResponseScheduleTemplateListInner;
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
 * SearchScheduleTemplateResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:00:28.871973+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchScheduleTemplateResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid SearchScheduleTemplateResponseScheduleTemplateListInner> scheduleTemplateList;

  public SearchScheduleTemplateResponse scheduleTemplateList(List<@Valid SearchScheduleTemplateResponseScheduleTemplateListInner> scheduleTemplateList) {
    this.scheduleTemplateList = scheduleTemplateList;
    return this;
  }

  public SearchScheduleTemplateResponse addScheduleTemplateListItem(SearchScheduleTemplateResponseScheduleTemplateListInner scheduleTemplateListItem) {
    if (this.scheduleTemplateList == null) {
      this.scheduleTemplateList = new ArrayList<>();
    }
    this.scheduleTemplateList.add(scheduleTemplateListItem);
    return this;
  }

  /**
   * Get scheduleTemplateList
   * @return scheduleTemplateList
  */
  @Valid 
  @Schema(name = "scheduleTemplateList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduleTemplateList")
  public List<@Valid SearchScheduleTemplateResponseScheduleTemplateListInner> getScheduleTemplateList() {
    return scheduleTemplateList;
  }

  public void setScheduleTemplateList(List<@Valid SearchScheduleTemplateResponseScheduleTemplateListInner> scheduleTemplateList) {
    this.scheduleTemplateList = scheduleTemplateList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchScheduleTemplateResponse searchScheduleTemplateResponse = (SearchScheduleTemplateResponse) o;
    return Objects.equals(this.scheduleTemplateList, searchScheduleTemplateResponse.scheduleTemplateList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduleTemplateList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchScheduleTemplateResponse {\n");
    sb.append("    scheduleTemplateList: ").append(toIndentedString(scheduleTemplateList)).append("\n");
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

