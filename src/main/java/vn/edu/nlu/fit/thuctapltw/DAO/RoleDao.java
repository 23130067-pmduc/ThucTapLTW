package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Role;

import java.util.List;

public class RoleDao extends BaseDao {

    public List<Role> getAllRoles() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
            SELECT id, name, description
            FROM roles
            ORDER BY id ASC""")
                .mapToBean(Role.class)
                .list());
    }
}
