package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.CreateBookingRequestPassengerListInner;
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
 * CreateBookingRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class CreateBookingRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer userId;

  private Integer scheduleId;

  private Integer totalPrice;

  @Valid
  private List<@Valid CreateBookingRequestPassengerListInner> passengerList;

  public CreateBookingRequest userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
  */
  
  @Schema(name = "user_id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("user_id")
  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public CreateBookingRequest scheduleId(Integer scheduleId) {
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

  public CreateBookingRequest totalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
    return this;
  }

  /**
   * Get totalPrice
   * @return totalPrice
  */
  
  @Schema(name = "totalPrice", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("totalPrice")
  public Integer getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(Integer totalPrice) {
    this.totalPrice = totalPrice;
  }

  public CreateBookingRequest passengerList(List<@Valid CreateBookingRequestPassengerListInner> passengerList) {
    this.passengerList = passengerList;
    return this;
  }

  public CreateBookingRequest addPassengerListItem(CreateBookingRequestPassengerListInner passengerListItem) {
    if (this.passengerList == null) {
      this.passengerList = new ArrayList<>();
    }
    this.passengerList.add(passengerListItem);
    return this;
  }

  /**
   * Get passengerList
   * @return passengerList
  */
  @Valid 
  @Schema(name = "passengerList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("passengerList")
  public List<@Valid CreateBookingRequestPassengerListInner> getPassengerList() {
    return passengerList;
  }

  public void setPassengerList(List<@Valid CreateBookingRequestPassengerListInner> passengerList) {
    this.passengerList = passengerList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CreateBookingRequest createBookingRequest = (CreateBookingRequest) o;
    return Objects.equals(this.userId, createBookingRequest.userId) &&
        Objects.equals(this.scheduleId, createBookingRequest.scheduleId) &&
        Objects.equals(this.totalPrice, createBookingRequest.totalPrice) &&
        Objects.equals(this.passengerList, createBookingRequest.passengerList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, scheduleId, totalPrice, passengerList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CreateBookingRequest {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    scheduleId: ").append(toIndentedString(scheduleId)).append("\n");
    sb.append("    totalPrice: ").append(toIndentedString(totalPrice)).append("\n");
    sb.append("    passengerList: ").append(toIndentedString(passengerList)).append("\n");
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

