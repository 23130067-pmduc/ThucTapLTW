package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.PermissionDao;
import vn.edu.nlu.fit.thuctapltw.model.Permission;

import java.util.List;
import java.util.Set;

public class PermissionService {
    private final PermissionDao permissionDao = new PermissionDao();

    public List<Permission> getAllPermissions() {
        return permissionDao.getAllPermission();
    }

    public Set<String> getPermissionNamesByRoleId(int roleId){
        return permissionDao.getPermissionNamesByRoleId(roleId);
    }

    public void updatePermissionsForRole(int roleId, List<Integer> permissionIds) {
        permissionDao.updatePermissionsForRole(roleId, permissionIds);
    }
}
