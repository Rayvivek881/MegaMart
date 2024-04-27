package org.rayvivek881.megamart.repositories;

import org.rayvivek881.megamart.documents.OrderElement;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderElementRepository extends MongoRepository<OrderElement, String>{

}
