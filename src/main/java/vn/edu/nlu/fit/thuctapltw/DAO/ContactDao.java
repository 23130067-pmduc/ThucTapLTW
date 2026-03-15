package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Contact;

import java.util.List;

public class ContactDao extends BaseDao {
    public void addContact(Contact contact) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                INSERT INTO contacts(user_id, name, email, phone, address, message, status, created_at)
                VALUES (:userId, :name, :email, :phone, :address, :message, 'New', NOW())
                """
                ).bind("userId", contact.getUserId())
                .bind("name", contact.getName())
                .bind("email", contact.getEmail())
                .bind("phone", contact.getPhone())
                .bind("address", contact.getAddress())
                .bind("message", contact.getMessage())
                .execute());
    }

    public List<Contact> getAllContact() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM contacts""")
                .mapToBean(Contact.class)
                .list());
    }


    public List<Contact> getAllContactByStatus(String status) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM contacts
                WHERE status = :status""")
                .bind("status",status)
                .mapToBean(Contact.class)
                .list());
    }

    public Contact getContactById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM contacts
                WHERE id = :id""")
                .bind("id", id)
                .mapToBean(Contact.class)
                .findOne()
                .orElse(null));
    }

    public void createContact(Contact contact) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                INSERT INTO contacts (name, email, phone, message, address, status, created_at) 
                VALUES (:name, :email, :phone, :message, :address, :status, NOW())""")
                .bind("userId", contact.getUserId())
                .bind("name", contact.getName())
                .bind("email", contact.getEmail())
                .bind("phone", contact.getPhone())
                .bind("message", contact.getMessage())
                .bind("address", contact.getAddress())
                .bind("status", contact.getStatus())
                .execute());
    }

    public void updateContact(Contact contact) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE contacts
                SET 
                    name = :name,
                    email = :email,
                    phone = :phone,
                    message = :message,
                    address = :address,
                    status = :status
                WHERE id = :id""")
                .bind("name", contact.getName())
                .bind("email", contact.getEmail())
                .bind("phone", contact.getPhone())
                .bind("message", contact.getMessage())
                .bind("address", contact.getAddress())
                .bind("status", contact.getStatus())
                .bind("id", contact.getId())
                .execute());
    }


    public void acceptContact(int id, String status) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE contacts
                SET status = :status
                WHERE id = :id""")
                .bind("id", id)
                .bind("status", status)
                .execute());
    }
}
