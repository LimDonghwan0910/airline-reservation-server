package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.GetFlightsResponseFlightsListInner;
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
 * GetFlightsResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetFlightsResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid GetFlightsResponseFlightsListInner> flightsList;

  public GetFlightsResponse flightsList(List<@Valid GetFlightsResponseFlightsListInner> flightsList) {
    this.flightsList = flightsList;
    return this;
  }

  public GetFlightsResponse addFlightsListItem(GetFlightsResponseFlightsListInner flightsListItem) {
    if (this.flightsList == null) {
      this.flightsList = new ArrayList<>();
    }
    this.flightsList.add(flightsListItem);
    return this;
  }

  /**
   * Get flightsList
   * @return flightsList
  */
  @Valid 
  @Schema(name = "FlightsList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("FlightsList")
  public List<@Valid GetFlightsResponseFlightsListInner> getFlightsList() {
    return flightsList;
  }

  public void setFlightsList(List<@Valid GetFlightsResponseFlightsListInner> flightsList) {
    this.flightsList = flightsList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetFlightsResponse getFlightsResponse = (GetFlightsResponse) o;
    return Objects.equals(this.flightsList, getFlightsResponse.flightsList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flightsList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetFlightsResponse {\n");
    sb.append("    flightsList: ").append(toIndentedString(flightsList)).append("\n");
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

