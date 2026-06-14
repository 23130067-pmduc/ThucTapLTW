package vn.edu.nlu.fit.thuctapltw.Controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.fit.thuctapltw.Service.PermissionService;
import vn.edu.nlu.fit.thuctapltw.Service.RoleService;
import vn.edu.nlu.fit.thuctapltw.model.Permission;
import vn.edu.nlu.fit.thuctapltw.model.Role;

import java.io.IOException;
import java.util.*;

@WebServlet("/permission-admin")
public class PermissionAdminController extends HttpServlet {

    private RoleService roleService;
    private PermissionService permissionService;

    @Override
    public void init(){
        roleService = new RoleService();
        permissionService = new PermissionService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Role> roles = roleService.getAllRoles();
        List<Permission> permissions = permissionService.getAllPermissions();
        Map<String, List<Permission>> permissionGroups = groupPermissions(permissions);

        int countRoles = roles.size();

        int selectedRoleId = 0;
        String selectedRoleName = "";

        if (!roles.isEmpty()) {
            selectedRoleId = roles.get(0).getId();
            selectedRoleName = roles.get(0).getName();
        }

        String roleIdParam = request.getParameter("roleId");
        if (roleIdParam != null && !roleIdParam.trim().isEmpty()) {
            try {
                selectedRoleId = Integer.parseInt(roleIdParam);
            } catch (NumberFormatException ignored) {
            }
        }

        for (Role role : roles) {
            if (role.getId() == selectedRoleId) {
                selectedRoleName = role.getName();
                break;
            }
        }

        Set<String> selectedRolePermissions = permissionService.getPermissionNamesByRoleId(selectedRoleId);


        request.setAttribute("roles", roles);
        request.setAttribute("countRoles", countRoles);
        request.setAttribute("permissions", permissions);
        request.setAttribute("permissionGroups", permissionGroups);
        request.setAttribute("selectedRoleId", selectedRoleId);
        request.setAttribute("selectedRoleName", selectedRoleName);
        request.setAttribute("selectedRolePermissions", selectedRolePermissions);

        request.getRequestDispatcher("/permission-admin.jsp").forward(request, response);
    }





    private Map<String, List<Permission>> groupPermissions(List<Permission> permissions) {
        Map<String, List<Permission>> groups = new LinkedHashMap<>();

        groups.put("Thống kê & báo cáo", new ArrayList<>());
        groups.put("Sản phẩm", new ArrayList<>());
        groups.put("Kho hàng", new ArrayList<>());
        groups.put("Hoàn hàng", new ArrayList<>());
        groups.put("Danh mục", new ArrayList<>());
        groups.put("Đơn hàng", new ArrayList<>());
        groups.put("Người dùng", new ArrayList<>());
        groups.put("Banner", new ArrayList<>());
        groups.put("Tin tức", new ArrayList<>());
        groups.put("Mã giảm giá & khuyến mãi", new ArrayList<>());
        groups.put("Thông báo & liên hệ", new ArrayList<>());
        groups.put("Phân quyền & vai trò", new ArrayList<>());
        groups.put("Hồ sơ", new ArrayList<>());
        groups.put("Khác", new ArrayList<>());

        for (Permission permission : permissions) {
            String name = permission.getName();

            if (name.startsWith("DASHBOARD") || name.startsWith("REPORT")) {
                groups.get("Thống kê & báo cáo").add(permission);
            } else if (name.startsWith("PRODUCT")) {
                groups.get("Sản phẩm").add(permission);
            } else if (name.startsWith("WAREHOUSE")
                    || name.startsWith("IMPORT_RECEIPT")
                    || name.startsWith("EXPORT_RECEIPT")
                    || name.startsWith("STOCK")) {
                groups.get("Kho hàng").add(permission);
            } else if (name.startsWith("RETURN_RECEIPT")) {
                groups.get("Hoàn hàng").add(permission);
            } else if (name.startsWith("CATEGORY")) {
                groups.get("Danh mục").add(permission);
            } else if (name.startsWith("ORDER")) {
                groups.get("Đơn hàng").add(permission);
            } else if (name.startsWith("USER")) {
                groups.get("Người dùng").add(permission);
            } else if (name.startsWith("BANNER")) {
                groups.get("Banner").add(permission);
            } else if (name.startsWith("NEWS")) {
                groups.get("Tin tức").add(permission);
            } else if (name.startsWith("VOUCHER") || name.startsWith("PROMOTION_EVENT")) {
                groups.get("Mã giảm giá & khuyến mãi").add(permission);
            } else if (name.startsWith("NOTIFICATION") || name.startsWith("CONTACT")) {
                groups.get("Thông báo & liên hệ").add(permission);
            } else if (name.startsWith("ROLE") || name.startsWith("PERMISSION")) {
                groups.get("Phân quyền & vai trò").add(permission);
            } else if (name.startsWith("PROFILE")) {
                groups.get("Hồ sơ").add(permission);
            } else {
                groups.get("Khác").add(permission);
            }
        }

        groups.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return groups;
    }

}