package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.fit.thuctapltw.Service.ContactService;
import vn.edu.nlu.fit.thuctapltw.model.Contact;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ContactAdminController", value = "/contact-admin")
public class ContactAdminController extends HttpServlet {

    private ContactService contactService;

    @Override
    public void init() {
        contactService = new ContactService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String mode = request.getParameter("mode");

        if (mode == null) {
            List<Contact> contacts = contactService.getAllContact();

            int total = contacts.size();

            int totalNew = contactService.getAllContactByStatus("New").size();
            int totalProcessing = contactService.getAllContactByStatus("Processing").size();
            int totalClosed = contactService.getAllContactByStatus("Closed").size();

            request.setAttribute("contacts", contacts);
            request.setAttribute("total", total);
            request.setAttribute("totalNew", totalNew);
            request.setAttribute("totalProcessing", totalProcessing);
            request.setAttribute("totalClosed", totalClosed);

            request.getRequestDispatcher("/contact-admin.jsp").forward(request, response);
            return;

        }

        if ("edit".equals(mode) || "view".equals(mode)) {
            int id = Integer.parseInt(request.getParameter("id"));

            Contact contact = contactService.getContactById(id);

            request.setAttribute("contact", contact);
            request.setAttribute("mode", mode);

            request.getRequestDispatcher("contact-form.jsp").forward(request, response);
            return;
        }

        if ("add".equals(mode)) {
            request.setAttribute("mode", mode);
            request.getRequestDispatcher("contact-form.jsp").forward(request, response);
            return;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if ("create".equals(action)) {
            Contact contact = new Contact();

            contact.setName(request.getParameter("name"));
            contact.setEmail(request.getParameter("email"));
            contact.setPhone(request.getParameter("phone"));
            contact.setStatus(request.getParameter("status"));
            contact.setAddress(request.getParameter("address"));
            contact.setMessage(request.getParameter("message"));

            contactService.createContact(contact);

            response.sendRedirect("contact-admin");
            return;
        }

        if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Contact contact = new Contact();

            contact.setId(id);
            contact.setName(request.getParameter("name"));
            contact.setEmail(request.getParameter("email"));
            contact.setPhone(request.getParameter("phone"));
            contact.setStatus(request.getParameter("status"));
            contact.setAddress(request.getParameter("address"));
            contact.setMessage(request.getParameter("message"));

            contactService.updateContact(contact);
            response.sendRedirect("contact-admin");
            return;
        }

        if ("accept".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));

            contactService.acceptContact(id);

            response.sendRedirect("contact-admin");
            return;
        }
    }

}