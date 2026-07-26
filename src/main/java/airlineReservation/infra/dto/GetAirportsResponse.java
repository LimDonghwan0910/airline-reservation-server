package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.GetAirportsResponseAirportListInner;
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
 * GetAirportsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:00:28.871973+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetAirportsResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid GetAirportsResponseAirportListInner> airportList;

  public GetAirportsResponse airportList(List<@Valid GetAirportsResponseAirportListInner> airportList) {
    this.airportList = airportList;
    return this;
  }

  public GetAirportsResponse addAirportListItem(GetAirportsResponseAirportListInner airportListItem) {
    if (this.airportList == null) {
      this.airportList = new ArrayList<>();
    }
    this.airportList.add(airportListItem);
    return this;
  }

  /**
   * Get airportList
   * @return airportList
  */
  @Valid 
  @Schema(name = "airportList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airportList")
  public List<@Valid GetAirportsResponseAirportListInner> getAirportList() {
    return airportList;
  }

  public void setAirportList(List<@Valid GetAirportsResponseAirportListInner> airportList) {
    this.airportList = airportList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAirportsResponse getAirportsResponse = (GetAirportsResponse) o;
    return Objects.equals(this.airportList, getAirportsResponse.airportList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAirportsResponse {\n");
    sb.append("    airportList: ").append(toIndentedString(airportList)).append("\n");
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

