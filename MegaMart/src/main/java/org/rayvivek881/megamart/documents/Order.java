package org.rayvivek881.megamart.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;
import java.util.List;

@Data
@Builder
@Document(collection = "Orders")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

  @MongoId
  private String _id;

  private String buyerId;
  private String SellerId;
  private String addressId;

  private String status;
  private double total;
  private List<String> trackHistory;

  private Date deliveryDate;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
