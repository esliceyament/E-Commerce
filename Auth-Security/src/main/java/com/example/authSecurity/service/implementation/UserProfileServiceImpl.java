package com.example.authSecurity.service.implementation;

import com.esliceyament.shared.payload.ShippingAddressUpdate;
import com.example.authSecurity.dto.AddressDto;
import com.example.authSecurity.dto.UserProfileDto;
import com.example.authSecurity.entity.Address;
import com.example.authSecurity.entity.User;
import com.example.authSecurity.entity.UserPrincipal;
import com.example.authSecurity.entity.UserProfile;
import com.example.authSecurity.exception.ProfileNotFoundException;
import com.example.authSecurity.mapper.UserProfileMapper;
import com.example.authSecurity.repository.AddressRepository;
import com.example.authSecurity.repository.UserProfileRepository;
import com.example.authSecurity.repository.UserRepository;
import com.example.authSecurity.service.UserProfileService;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;


    public UserProfileDto createOrUpdateUserProfile(UserProfileDto dto) {
        UserProfile userProfile = userProfileRepository.findByUserId(getCurrentUserId())
                .orElseThrow();
        userProfile.setDateOfBirth(dto.getDateOfBirth());
        userProfileRepository.save(userProfile);
        return userProfileMapper.toDto(userProfile);
    }
 //////
    public AddressDto addAddress(AddressDto dto) {
        UserProfile userProfile = userProfileRepository.findByUserId(getCurrentUserId())
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Address address = new Address();
        address.setStreet(dto.getStreet());
        address.setCity(dto.getCity());
        address.setPostalCode(dto.getPostalCode());
        address.setCountry(dto.getCountry());
        address.setLastUsedAt(new Date());
        address.setUserProfile(userProfile);
        //addressRepository.save(address);
        userProfile.getAddress().add(address);
        userProfile.getAddress().sort(Comparator.comparing(Address::getLastUsedAt));
        userProfileRepository.save(userProfile);

        return userProfileMapper.addressToDto(address);
    }

    @Transactional
    public void addShippingAddress(ShippingAddressUpdate shippingAddressUpdate) {
        User user = userRepository.findByUsername(shippingAddressUpdate.getUsername())
                .orElseThrow(() -> new NotFoundException("User " + shippingAddressUpdate.getUsername() + " not found!"));
        UserProfile userProfile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Address address = new Address();
        address.setStreet(shippingAddressUpdate.getStreet());
        address.setCity(shippingAddressUpdate.getCity());
        address.setPostalCode(shippingAddressUpdate.getPostalCode());
        address.setCountry(shippingAddressUpdate.getCountry());
        address.setLastUsedAt(new Date());
        address.setUserProfile(userProfile);
        userProfile.getAddress().add(address);
        userProfile.getAddress().sort(Comparator.comparing(Address::getLastUsedAt));
        userProfileRepository.save(userProfile);
    }

    public UserProfileDto getUserProfile() {
        Long userId = getCurrentUserId();
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for userId: " + userId));
        return userProfileMapper.toDto(userProfile);
    }

    public Long getCurrentUserId() {
        UserPrincipal userId = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userId.user().getId();
    }

    public AddressDto getAddress(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User " + username + " not found!"));

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found!"));

        List<Address> addresses = profile.getAddress();
        if (addresses.isEmpty()) {
            return null;
        }
        Address selectedAddress = addresses.get(addresses.size() - 1);
        AddressDto addressDto = new AddressDto();
        addressDto.setStreet(selectedAddress.getStreet());
        addressDto.setCity(selectedAddress.getCity());
        addressDto.setPostalCode(selectedAddress.getPostalCode());
        addressDto.setCountry(selectedAddress.getCountry());
        return addressDto;
    }

    public AddressDto getAddressById(String token, Long id) {
        String username = jwtUtil.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User " + username + " not found!"));

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new NotFoundException("User not found!"));
        Address selectedAddress = profile.getAddress().stream()
                .filter(address -> address.getId().equals(id))
                .findFirst().orElse(null);
        if (selectedAddress == null) {
            return null;
        }

        AddressDto addressDto = new AddressDto();
        addressDto.setStreet(selectedAddress.getStreet());
        addressDto.setCity(selectedAddress.getCity());
        addressDto.setPostalCode(selectedAddress.getPostalCode());
        addressDto.setCountry(selectedAddress.getCountry());
        return addressDto;
    }

    public void updateDefaultAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                        .orElseThrow(() -> new NotFoundException("Address not found!"));
        address.setLastUsedAt(new Date());
        Long userId = address.getUserProfile().getUserId();
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("User not found!"));

        profile.getAddress().sort(Comparator.comparing(Address::getLastUsedAt));
        userProfileRepository.save(profile);


    }
}
