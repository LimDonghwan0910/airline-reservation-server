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
 * GetAirportsResponseAirportListInner
 */

@JsonTypeName("GetAirportsResponse_airportList_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-08-11T10:31:14.711529+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class GetAirportsResponseAirportListInner implements Serializable {

  private static final long serialVersionUID = 1L;

  private String airportId;

  private String airportNameKo;

  private String airportNameEn;

  private String country;

  private String city;

  public GetAirportsResponseAirportListInner airportId(String airportId) {
    this.airportId = airportId;
    return this;
  }

  /**
   * Get airportId
   * @return airportId
  */
  
  @Schema(name = "airportId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airportId")
  public String getAirportId() {
    return airportId;
  }

  public void setAirportId(String airportId) {
    this.airportId = airportId;
  }

  public GetAirportsResponseAirportListInner airportNameKo(String airportNameKo) {
    this.airportNameKo = airportNameKo;
    return this;
  }

  /**
   * Get airportNameKo
   * @return airportNameKo
  */
  
  @Schema(name = "airportNameKo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airportNameKo")
  public String getAirportNameKo() {
    return airportNameKo;
  }

  public void setAirportNameKo(String airportNameKo) {
    this.airportNameKo = airportNameKo;
  }

  public GetAirportsResponseAirportListInner airportNameEn(String airportNameEn) {
    this.airportNameEn = airportNameEn;
    return this;
  }

  /**
   * Get airportNameEn
   * @return airportNameEn
  */
  
  @Schema(name = "airportNameEn", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airportNameEn")
  public String getAirportNameEn() {
    return airportNameEn;
  }

  public void setAirportNameEn(String airportNameEn) {
    this.airportNameEn = airportNameEn;
  }

  public GetAirportsResponseAirportListInner country(String country) {
    this.country = country;
    return this;
  }

  /**
   * Get country
   * @return country
  */
  
  @Schema(name = "country", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("country")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public GetAirportsResponseAirportListInner city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
  */
  
  @Schema(name = "city", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GetAirportsResponseAirportListInner getAirportsResponseAirportListInner = (GetAirportsResponseAirportListInner) o;
    return Objects.equals(this.airportId, getAirportsResponseAirportListInner.airportId) &&
        Objects.equals(this.airportNameKo, getAirportsResponseAirportListInner.airportNameKo) &&
        Objects.equals(this.airportNameEn, getAirportsResponseAirportListInner.airportNameEn) &&
        Objects.equals(this.country, getAirportsResponseAirportListInner.country) &&
        Objects.equals(this.city, getAirportsResponseAirportListInner.city);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airportId, airportNameKo, airportNameEn, country, city);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GetAirportsResponseAirportListInner {\n");
    sb.append("    airportId: ").append(toIndentedString(airportId)).append("\n");
    sb.append("    airportNameKo: ").append(toIndentedString(airportNameKo)).append("\n");
    sb.append("    airportNameEn: ").append(toIndentedString(airportNameEn)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
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

