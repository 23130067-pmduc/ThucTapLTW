package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Address;

import java.util.List;

public class AddressDao extends BaseDao {

    public List<Address> getByUser(int userId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT
                    id,
                    user_id AS userId,
                    receiver_name AS receiverName,
                    phone,
                    city,
                    district,
                    ward,
                    detail_address AS detailAddress,
                    is_default AS defaultAddress,
                    is_active AS active
                FROM addresses
                WHERE user_id = :userId
                  AND is_active = 1
                ORDER BY is_default DESC, id DESC
                """)
                .bind("userId", userId)
                .mapToBean(Address.class)
                .list());
    }

    public Address findById(int id, int userId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT
                    id,
                    user_id AS userId,
                    receiver_name AS receiverName,
                    phone,
                    city,
                    district,
                    ward,
                    detail_address AS detailAddress,
                    is_default AS defaultAddress,
                    is_active AS active
                FROM addresses
                WHERE id = :id
                  AND user_id = :userId
                """)
                .bind("id", id)
                .bind("userId", userId)
                .mapToBean(Address.class)
                .findOne()
                .orElse(null));
    }

    public int countActiveByUser(int userId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM addresses
                WHERE user_id = :userId
                  AND is_active = 1
                """)
                .bind("userId", userId)
                .mapTo(Integer.class)
                .one());
    }

    public Address findFirstActiveByUser(int userId) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT
                    id,
                    user_id AS userId,
                    receiver_name AS receiverName,
                    phone,
                    city,
                    district,
                    ward,
                    detail_address AS detailAddress,
                    is_default AS defaultAddress,
                    is_active AS active
                FROM addresses
                WHERE user_id = :userId
                  AND is_active = 1
                ORDER BY id DESC
                LIMIT 1
                """)
                .bind("userId", userId)
                .mapToBean(Address.class)
                .findOne()
                .orElse(null));
    }

    public void clearDefaultByUser(int userId) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE addresses
                SET is_default = 0
                WHERE user_id = :userId
                  AND is_active = 1
                """)
                .bind("userId", userId)
                .execute());
    }

    public void insert(Address address) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                INSERT INTO addresses (
                    user_id,
                    receiver_name,
                    phone,
                    city,
                    district,
                    ward,
                    detail_address,
                    is_default,
                    is_active
                ) VALUES (
                    :userId,
                    :receiverName,
                    :phone,
                    :city,
                    :district,
                    :ward,
                    :detailAddress,
                    :defaultAddress,
                    1
                )
                """)
                .bind("userId", address.getUserId())
                .bind("receiverName", address.getReceiverName())
                .bind("phone", address.getPhone())
                .bind("city", address.getCity())
                .bind("district", address.getDistrict())
                .bind("ward", address.getWard())
                .bind("detailAddress", address.getDetailAddress())
                .bind("defaultAddress", address.isDefaultAddress() ? 1 : 0)
                .execute());
    }

    public void update(Address address) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE addresses
                SET receiver_name = :receiverName,
                    phone = :phone,
                    city = :city,
                    district = :district,
                    ward = :ward,
                    detail_address = :detailAddress,
                    is_default = :defaultAddress
                WHERE id = :id
                  AND user_id = :userId
                  AND is_active = 1
                """)
                .bind("id", address.getId())
                .bind("userId", address.getUserId())
                .bind("receiverName", address.getReceiverName())
                .bind("phone", address.getPhone())
                .bind("city", address.getCity())
                .bind("district", address.getDistrict())
                .bind("ward", address.getWard())
                .bind("detailAddress", address.getDetailAddress())
                .bind("defaultAddress", address.isDefaultAddress() ? 1 : 0)
                .execute());
    }

    public void setDefault(int id, int userId) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE addresses
                SET is_default = 1
                WHERE id = :id
                  AND user_id = :userId
                  AND is_active = 1
                """)
                .bind("id", id)
                .bind("userId", userId)
                .execute());
    }

    public void softDelete(int id, int userId) {
        getJdbi().useHandle(handle -> handle.createUpdate("""
                UPDATE addresses
                SET is_active = 0,
                    is_default = 0
                WHERE id = :id
                  AND user_id = :userId
                  AND is_active = 1
                """)
                .bind("id", id)
                .bind("userId", userId)
                .execute());
    }
}
