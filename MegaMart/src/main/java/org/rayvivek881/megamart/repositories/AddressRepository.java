package org.rayvivek881.megamart.repositories;

import org.rayvivek881.megamart.documents.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AddressRepository extends MongoRepository<Address, String>{
  List<Address> getAddressByUserId(String userId);
}
