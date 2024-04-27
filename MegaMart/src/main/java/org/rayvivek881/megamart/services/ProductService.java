package org.rayvivek881.megamart.services;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.rayvivek881.megamart.documents.Product;
import org.rayvivek881.megamart.repositories.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final MongoTemplate mongoTemplate;

  public Product save(Product product) {
    return productRepository.save(product);
  }

  public Product getProductById(String productId) {
    return productRepository.findById(productId).orElse(null);
  }

  public List<Product> getProductBySellerId(String sellerId, int skip, int limit) {
    Query query = new Query(Criteria.where("sellerId").is(sellerId));
    query.with(Sort.by(Sort.Direction.DESC, "createdAt"));

    query.skip(skip).limit(limit);

    return mongoTemplate.find(query, Product.class);
  }

  public List<Product> searchByKeyword(String text) {
    String regex = String.format(".*%s.*", text);
    List<String> fields = List.of("name", "category", "description");

    Criteria[] criteriaArray = new Criteria[fields.size()];
    for (int i = 0; i < fields.size(); i++) {
      criteriaArray[i] = Criteria.where(fields.get(i)).regex(regex, "i");
    }
    Criteria criteria = new Criteria().orOperator(criteriaArray);

    return mongoTemplate.find(new Query(criteria), Product.class);
  }
}
