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
 * GetSeatsResponseSeatListInner
 */

@JsonTypeName("GetSeatsResponse_seatList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetSeatsResponseSeatListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private Integer scheduledSeatNo;

  private String seatName;

  private String status;

  public GetSeatsResponseSeatListInner scheduledSeatNo(Integer scheduledSeatNo) {
    this.scheduledSeatNo = scheduledSeatNo;
    return this;
  }

  /**
   * Get scheduledSeatNo
   * @return scheduledSeatNo
  */
  
  @Schema(name = "scheduledSeatNo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("scheduledSeatNo")
  public Integer getScheduledSeatNo() {
    return scheduledSeatNo;
  }

  public void setScheduledSeatNo(Integer scheduledSeatNo) {
    this.scheduledSeatNo = scheduledSeatNo;
  }

  public GetSeatsResponseSeatListInner seatName(String seatName) {
    this.seatName = seatName;
    return this;
  }

  /**
   * Get seatName
   * @return seatName
  */
  
  @Schema(name = "seatName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("seatName")
  public String getSeatName() {
    return seatName;
  }

  public void setSeatName(String seatName) {
    this.seatName = seatName;
  }

  public GetSeatsResponseSeatListInner status(String status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  
  @Schema(name = "status", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetSeatsResponseSeatListInner getSeatsResponseSeatListInner = (GetSeatsResponseSeatListInner) o;
    return Objects.equals(this.scheduledSeatNo, getSeatsResponseSeatListInner.scheduledSeatNo) &&
        Objects.equals(this.seatName, getSeatsResponseSeatListInner.seatName) &&
        Objects.equals(this.status, getSeatsResponseSeatListInner.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scheduledSeatNo, seatName, status);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetSeatsResponseSeatListInner {\n");
    sb.append("    scheduledSeatNo: ").append(toIndentedString(scheduledSeatNo)).append("\n");
    sb.append("    seatName: ").append(toIndentedString(seatName)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
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

