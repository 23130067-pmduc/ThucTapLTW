package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.AddressDao;
import vn.edu.nlu.fit.thuctapltw.model.Address;

import java.util.List;

public class AddressService {
    private final AddressDao addressDao = new AddressDao();

    public List<Address> getAddressesByUser(int userId) {
        return addressDao.getByUser(userId);
    }

    public Address getAddressById(int addressId, int userId) {
        return addressDao.findById(addressId, userId);
    }

    public void addAddress(Address address) {
        validateAddress(address);

        if (addressDao.countActiveByUser(address.getUserId()) == 0) {
            address.setDefaultAddress(true);
        }

        if (address.isDefaultAddress()) {
            addressDao.clearDefaultByUser(address.getUserId());
        }

        addressDao.insert(address);
    }

    public void updateAddress(Address address) {
        validateAddress(address);

        Address existingAddress = addressDao.findById(address.getId(), address.getUserId());
        if (existingAddress == null || !existingAddress.isActive()) {
            throw new RuntimeException("Địa chỉ không tồn tại hoặc không thuộc tài khoản của bạn");
        }

        if (address.isDefaultAddress()) {
            addressDao.clearDefaultByUser(address.getUserId());
        } else if (existingAddress.isDefaultAddress()) {
            address.setDefaultAddress(true);
        }

        addressDao.update(address);
    }

    public void setDefaultAddress(int addressId, int userId) {
        Address existingAddress = addressDao.findById(addressId, userId);
        if (existingAddress == null || !existingAddress.isActive()) {
            throw new RuntimeException("Địa chỉ không tồn tại hoặc không thuộc tài khoản của bạn");
        }

        addressDao.clearDefaultByUser(userId);
        addressDao.setDefault(addressId, userId);
    }

    public void deleteAddress(int addressId, int userId) {
        Address existingAddress = addressDao.findById(addressId, userId);
        if (existingAddress == null || !existingAddress.isActive()) {
            throw new RuntimeException("Địa chỉ không tồn tại hoặc không thuộc tài khoản của bạn");
        }

        addressDao.softDelete(addressId, userId);

        if (existingAddress.isDefaultAddress()) {
            Address fallbackAddress = addressDao.findFirstActiveByUser(userId);
            if (fallbackAddress != null) {
                addressDao.clearDefaultByUser(userId);
                addressDao.setDefault(fallbackAddress.getId(), userId);
            }
        }
    }

    private void validateAddress(Address address) {
        if (address == null) {
            throw new RuntimeException("Dữ liệu địa chỉ không hợp lệ");
        }
        if (isBlank(address.getReceiverName())) {
            throw new RuntimeException("Vui lòng nhập họ tên người nhận");
        }
        if (isBlank(address.getPhone())) {
            throw new RuntimeException("Vui lòng nhập số điện thoại");
        }
        if (!address.getPhone().trim().matches("^(0[3|5|7|8|9])[0-9]{8}$")) {
            throw new RuntimeException("Số điện thoại không hợp lệ");
        }
        if (isBlank(address.getCity())) {
            throw new RuntimeException("Vui lòng chọn tỉnh/thành phố");
        }
        if (isBlank(address.getDistrict())) {
            throw new RuntimeException("Vui lòng chọn quận/huyện");
        }
        if (isBlank(address.getWard())) {
            throw new RuntimeException("Vui lòng chọn phường/xã");
        }
        if (isBlank(address.getDetailAddress())) {
            throw new RuntimeException("Vui lòng nhập địa chỉ chi tiết");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
