package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ContactDao;
import vn.edu.nlu.fit.thuctapltw.model.Contact;

import java.util.List;

public class ContactService {
    private final ContactDao contactDao = new ContactDao();

    public void addContact(Contact contact) {
        contactDao.addContact(contact);
    }

    public List<Contact> getAllContact() {
        return contactDao.getAllContact();
    }



    public List<Contact> getAllContactByStatus(String status) {
        return contactDao.getAllContactByStatus(status);
    }

    public Contact getContactById(int id) {
        return contactDao.getContactById(id);
    }

    public void createContact(Contact contact) {
        contactDao.createContact(contact);
    }

    public void updateContact(Contact contact) {
        contactDao.updateContact(contact);
    }

    public void acceptContact(int id) {
        contactDao.acceptContact(id, "Closed");
    }
}
