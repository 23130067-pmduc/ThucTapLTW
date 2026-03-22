package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.UserDao;
import vn.edu.nlu.fit.thuctapltw.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import vn.edu.nlu.fit.thuctapltw.Util.PasswordUtil;

public class UserService {

    private final UserDao userDao = new UserDao();

    public User login(String username, String password) {
        User user = userDao.finduser(username);

        if (user == null) return null;

        if (!PasswordUtil.verify(password, user.getPassword())) return null;

        return user;
    }
    private String checkPasswordStrength(String password) {
        if (password == null || password.length() < 8)
            return "Mật khẩu phải có ít nhất 8 ký tự";

        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSpecial = true;
        }

        if (!hasUpper) return "Mật khẩu phải có ít nhất 1 chữ hoa";
        if (!hasLower) return "Mật khẩu phải có ít nhất 1 chữ thường";
        if (!hasDigit) return "Mật khẩu phải có ít nhất 1 chữ số";
        if (!hasSpecial) return "Mật khẩu phải có ít nhất 1 ký tự đặc biệt";

        return null;
    }


    public void registerSendOtp(String username, String email, String password) {

        // 1. Check username
        if (userDao.existsByUsername(username)) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại");
        }
        // 2. Check password
        String passwordError = checkPasswordStrength(password);
        if (passwordError != null) {
            throw new RuntimeException(passwordError);
        }

        String hashedPassword = PasswordUtil.hash(password);
        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        // 3. Check email
        User existing = userDao.finduser(email);

        if (existing == null) {
            // chưa tồn tại → insert pending
            userDao.insertPendingUser(username, email, hashedPassword, otp, expiredAt);
        } else if (existing.getIsActive() == 0) {
            // đã đăng ký nhưng chưa xác nhận → gửi lại OTP
            userDao.updateOtp(email, otp, expiredAt);
        } else {
            // đã active
            throw new RuntimeException("Email đã được đăng ký");
        }

        // 4. Send mail
        EmailService.sendEmail(
                email,
                "OTP xác nhận đăng ký",
                "<h3>Mã OTP của bạn: <b>" + otp + "</b></h3>"
        );
    }
    public boolean verifyOtp(String email, String otp) {
        return userDao.verifyOtp(email, otp);
    }

    public boolean verifyOtpForReset(String email, String otp) {
        return userDao.verifyOtpForReset(email, otp);
    }

    public boolean checkOtpCuoi(String email, String otp) {
        return userDao.checkOtpCuoi(email, otp);
    }

    public void sendOtpResetPassword(String email) {

        User user = userDao.finduser(email);
        if (user == null) {throw new RuntimeException("Email không tồn tại");
        }

        String otp = String.format("%06d", new Random().nextInt(1_000_000));
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

        userDao.updateOtpForReset(email, otp, expiredAt);

        EmailService.sendEmail(
                email, "OTP đặt lại mật khẩu", "<h3>Mã OTP của bạn: <b>" + otp + "</b></h3>"
        );
    }

    public void resetPassword(String email, String otp, String newPassword) {

        String error = checkPasswordStrength(newPassword);
        if (error != null) {
            throw new RuntimeException(error);
        }

        boolean ok = userDao.verifyOtpForReset(email, otp);
        if (!ok) {
            throw new RuntimeException("OTP sai hoặc đã hết hạn");
        }
        String hashed = PasswordUtil.hash(newPassword);
        userDao.updatePassword(email, hashed);
    }

    public User findById(int id) {
        return userDao.findUserById(id);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public boolean checkOldPass(int id, String oldPass) {
        String hashPass = userDao.getPasswordById(id);

        return PasswordUtil.checkOldPass(oldPass, hashPass);
    }

    public boolean updatePass(int id, String newPass) {
        String hash = PasswordUtil.hash(newPass);

        return userDao.updatePasss(id, hash);
    }

    public List<User> getAllUser() {
        return userDao.getListUser();
    }

    public int getCountInWeek() {
        return userDao.getCountInWeek();
    }

    public int getCountActive() {
        return userDao.getCountActive();
    }

    public int getCountBlock() {
        return userDao.getCountBlock();
    }

    public List<User> searchByUsernameOrEmail(String keyword) {
        return userDao.searchByUsernameOrEmail(keyword);
    }

    public void createUser(User user) {
        String pass = "Newpass123*";
        String hashed = PasswordUtil.hash(pass);
        user.setPassword(hashed);

        userDao.createUser(user);
    }

    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    public void blockUser(int id) {
        userDao.blockUser(id , "BLOCKED");
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public int createGoogleUser(User user) {
        return userDao.createGoogleUser(user);
    }
}
