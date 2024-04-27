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
@Document(collection = "Users")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

  @MongoId
  private String _id;

  private String username;
  private String password;

  private List<String> roles;

  @CreatedDate
  private Date createdAt;

  @LastModifiedDate
  private Date updatedAt;
}