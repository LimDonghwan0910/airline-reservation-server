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
 * CreateBookingRequestPassengerListInner
 */

@JsonTypeName("CreateBookingRequest_passengerList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class CreateBookingRequestPassengerListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private String seat;

  private String name;

  public CreateBookingRequestPassengerListInner seat(String seat) {
    this.seat = seat;
    return this;
  }

  /**
   * Get seat
   * @return seat
  */
  
  @Schema(name = "seat", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seat")
  public String getSeat() {
    return seat;
  }

  public void setSeat(String seat) {
    this.seat = seat;
  }

  public CreateBookingRequestPassengerListInner name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   * @return name
  */
  
  @Schema(name = "name", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateBookingRequestPassengerListInner createBookingRequestPassengerListInner = (CreateBookingRequestPassengerListInner) o;
    return Objects.equals(this.seat, createBookingRequestPassengerListInner.seat) &&
        Objects.equals(this.name, createBookingRequestPassengerListInner.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(seat, name);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBookingRequestPassengerListInner {\n");
    sb.append("    seat: ").append(toIndentedString(seat)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
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

