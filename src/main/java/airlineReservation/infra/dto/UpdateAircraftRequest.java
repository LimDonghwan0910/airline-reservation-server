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
 * UpdateAircraftRequest
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-30T17:47:13.211020+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class UpdateAircraftRequest implements Serializable {

  private static final long serialVersionUID = 1L;

  private String aircraftId;

  private String aircraftName;

  private Integer rowCount;

  private Integer columnCount;

  public UpdateAircraftRequest aircraftId(String aircraftId) {
    this.aircraftId = aircraftId;
    return this;
  }

  /**
   * Get aircraftId
   * @return aircraftId
  */
  
  @Schema(name = "aircraftId", example = "SEO123", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftId")
  public String getAircraftId() {
    return aircraftId;
  }

  public void setAircraftId(String aircraftId) {
    this.aircraftId = aircraftId;
  }

  public UpdateAircraftRequest aircraftName(String aircraftName) {
    this.aircraftName = aircraftName;
    return this;
  }

  /**
   * Get aircraftName
   * @return aircraftName
  */
  
  @Schema(name = "aircraftName", example = "Boeing-777", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftName")
  public String getAircraftName() {
    return aircraftName;
  }

  public void setAircraftName(String aircraftName) {
    this.aircraftName = aircraftName;
  }

  public UpdateAircraftRequest rowCount(Integer rowCount) {
    this.rowCount = rowCount;
    return this;
  }

  /**
   * Get rowCount
   * @return rowCount
  */
  
  @Schema(name = "rowCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("rowCount")
  public Integer getRowCount() {
    return rowCount;
  }

  public void setRowCount(Integer rowCount) {
    this.rowCount = rowCount;
  }

  public UpdateAircraftRequest columnCount(Integer columnCount) {
    this.columnCount = columnCount;
    return this;
  }

  /**
   * Get columnCount
   * @return columnCount
  */
  
  @Schema(name = "columnCount", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("columnCount")
  public Integer getColumnCount() {
    return columnCount;
  }

  public void setColumnCount(Integer columnCount) {
    this.columnCount = columnCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UpdateAircraftRequest updateAircraftRequest = (UpdateAircraftRequest) o;
    return Objects.equals(this.aircraftId, updateAircraftRequest.aircraftId) &&
        Objects.equals(this.aircraftName, updateAircraftRequest.aircraftName) &&
        Objects.equals(this.rowCount, updateAircraftRequest.rowCount) &&
        Objects.equals(this.columnCount, updateAircraftRequest.columnCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(aircraftId, aircraftName, rowCount, columnCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UpdateAircraftRequest {\n");
    sb.append("    aircraftId: ").append(toIndentedString(aircraftId)).append("\n");
    sb.append("    aircraftName: ").append(toIndentedString(aircraftName)).append("\n");
    sb.append("    rowCount: ").append(toIndentedString(rowCount)).append("\n");
    sb.append("    columnCount: ").append(toIndentedString(columnCount)).append("\n");
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

