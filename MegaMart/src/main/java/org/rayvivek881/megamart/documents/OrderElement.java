package org.rayvivek881.megamart.documents;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Builder
@Document(collection = "OrderElements")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderElement {
  @MongoId
  private String _id;
  private String orderId;

  private String productId;
  private int quantity;
  private double discount;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
