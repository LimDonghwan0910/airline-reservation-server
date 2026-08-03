package airlineReservation.infra.dto;

import java.net.URI;
import java.util.Objects;
import airlineReservation.infra.dto.SearchBookingResponseBookingListInner;
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
 * SearchBookingResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-07-26T22:46:28.426248+09:00[Asia/Tokyo]", comments = "Generator version: 7.4.0")
public class SearchBookingResponse implements Serializable {

  private static final long serialVersionUID = 1L;

  @Valid
  private List<@Valid SearchBookingResponseBookingListInner> bookingList;

  public SearchBookingResponse bookingList(List<@Valid SearchBookingResponseBookingListInner> bookingList) {
    this.bookingList = bookingList;
    return this;
  }

  public SearchBookingResponse addBookingListItem(SearchBookingResponseBookingListInner bookingListItem) {
    if (this.bookingList == null) {
      this.bookingList = new ArrayList<>();
    }
    this.bookingList.add(bookingListItem);
    return this;
  }

  /**
   * Get bookingList
   * @return bookingList
  */
  @Valid 
  @Schema(name = "bookingList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("bookingList")
  public List<@Valid SearchBookingResponseBookingListInner> getBookingList() {
    return bookingList;
  }

  public void setBookingList(List<@Valid SearchBookingResponseBookingListInner> bookingList) {
    this.bookingList = bookingList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchBookingResponse searchBookingResponse = (SearchBookingResponse) o;
    return Objects.equals(this.bookingList, searchBookingResponse.bookingList);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bookingList);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchBookingResponse {\n");
    sb.append("    bookingList: ").append(toIndentedString(bookingList)).append("\n");
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

