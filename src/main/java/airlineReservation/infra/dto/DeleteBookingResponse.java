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
 * DeleteBookingResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class DeleteBookingResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  private Boolean success;

  public DeleteBookingResponse success(Boolean success) {
    this.success = success;
    return this;
  }

  /**
   * Get success
   * @return success
  */
  
  @Schema(name = "success", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("success")
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeleteBookingResponse deleteBookingResponse = (DeleteBookingResponse) o;
    return Objects.equals(this.success, deleteBookingResponse.success);
  }

  @Override
  public int hashCode() {
    return Objects.hash(success);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeleteBookingResponse {\n");
    sb.append("    success: ").append(toIndentedString(success)).append("\n");
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

