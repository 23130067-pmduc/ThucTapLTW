package vn.edu.nlu.fit.thuctapltw.DAO;

import vn.edu.nlu.fit.thuctapltw.model.Permission;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionDao extends BaseDao{

    public List<Permission>  getAllPermission(){
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT id, name, description
                FROM permissions
                ORDER BY id ASC """)
                .mapToBean(Permission.class)
                .list());
    }


    public Set<String> getPermissionNamesByRoleId(int roleId) {
        List<String> permissions = getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT p.name
                FROM role_permissions rp
                JOIN permissions p ON rp.permission_id = p.id
                WHERE rp.role_id = :roleId
                ORDER BY p.id ASC
                """)
                .bind("roleId", roleId)
                .mapTo(String.class)
                .list());

        return new HashSet<>(permissions);
    }

    public void updatePermissionsForRole(int roleId, List<Integer> permissionIds) {
        getJdbi().useTransaction(handle -> {
            handle.createUpdate("""
                DELETE FROM role_permissions
                WHERE role_id = :roleId
                """)
                    .bind("roleId", roleId)
                    .execute();

            for (Integer permissionId : permissionIds) {
                handle.createUpdate("""
                    INSERT INTO role_permissions(role_id, permission_id)
                    VALUES (:roleId, :permissionId)
                    """)
                        .bind("roleId", roleId)
                        .bind("permissionId", permissionId)
                        .execute();
            }
        });
    }
}
