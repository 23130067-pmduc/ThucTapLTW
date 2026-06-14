package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.RoleDao;
import vn.edu.nlu.fit.thuctapltw.model.Role;

import java.util.List;

public class RoleService {
    private final RoleDao roleDao = new RoleDao();

    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }
}
