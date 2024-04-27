package org.rayvivek881.megamart.documents;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Builder
@Document(collection = "Addresses")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {

  @MongoId
  private String _id;

  private String userId;
  private String phone;
  private String zipcode;

  private String address;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}
