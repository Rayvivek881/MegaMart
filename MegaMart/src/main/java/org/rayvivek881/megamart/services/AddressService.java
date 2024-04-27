package org.rayvivek881.megamart.services;

import lombok.RequiredArgsConstructor;
import org.rayvivek881.megamart.documents.Address;
import org.rayvivek881.megamart.documents.User;
import org.rayvivek881.megamart.repositories.AddressRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {

  private final AddressRepository addressRepository;
  private final UserServices userServices;

  public Address save(Address address) {
    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);

    address.setUserId(user.get_id());

    return addressRepository.save(address);
  }

  public Address getAddressById(String addressId) {
    return addressRepository.findById(addressId).
        orElseThrow(() -> new RuntimeException("Address not found"));
  }

  public void deleteAddress(String addressId) {
    Address address = getAddressById(addressId);

    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    if (!address.getUserId().equals(user.get_id())) {
      throw new RuntimeException("You are not authorized to perform this operation");
    }
    addressRepository.deleteById(addressId);
  }

  public Address updateAddress(String addressId, Address address) {
    Address preAddress = getAddressById(addressId);

    String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    User user = userServices.getUserByUsername(username);
    if (!preAddress.getUserId().equals(user.get_id())) {
      throw new RuntimeException("You are not authorized to perform this operation");
    }

    address.setUserId(user.get_id());
    return addressRepository.save(address);
  }

  public List<Address> getAddressByUserId(String userId) {
    return addressRepository.getAddressByUserId(userId);
  }
}
