package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.SearchAircraftResponseAircraftListInner;
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
 * SearchAircraftResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchAircraftResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid SearchAircraftResponseAircraftListInner> aircraftList;

  public SearchAircraftResponse aircraftList(List<@Valid SearchAircraftResponseAircraftListInner> aircraftList) {
    this.aircraftList = aircraftList;
    return this;
  }

  public SearchAircraftResponse addAircraftListItem(SearchAircraftResponseAircraftListInner aircraftListItem) {
    if (this.aircraftList == null) {
      this.aircraftList = new ArrayList<>();
    }
    this.aircraftList.add(aircraftListItem);
    return this;
  }

  /**
   * Get aircraftList
   * @return aircraftList
  */
  @Valid 
  @Schema(name = "aircraftList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftList")
  public List<@Valid SearchAircraftResponseAircraftListInner> getAircraftList() {
    return aircraftList;
  }

  public void setAircraftList(List<@Valid SearchAircraftResponseAircraftListInner> aircraftList) {
    this.aircraftList = aircraftList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchAircraftResponse searchAircraftResponse = (SearchAircraftResponse) o;
    return Objects.equals(this.aircraftList, searchAircraftResponse.aircraftList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aircraftList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchAircraftResponse {\n");
    sb.append("    aircraftList: ").append(toIndentedString(aircraftList)).append("\n");
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

