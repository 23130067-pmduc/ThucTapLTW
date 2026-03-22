package vn.edu.nlu.fit.thuctapltw.DAO;
import java.time.LocalDateTime;
import java.util.List;

import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import vn.edu.nlu.fit.thuctapltw.model.User;

public class UserDao extends BaseDao {
    public User finduser(String username) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("select * from users where username=:username OR email=:username")
                        .bind("username", username).mapToBean(User.class).findFirst().orElse(null)
        );
    }

    public List<User> getListUser() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("SELECT * FROM users")
                        .mapToBean(User.class)
                        .list()
        );
    }
    public void addUser(String username, String email, String password) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("insert into users (username,email,password) values(:username, :email, :password)").bind("username", username).bind("email", email).bind("password", password).execute()
        );
    }

    public void updatePass(String password) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("update users  set password=:password").bind("password", password).execute()
        );
    }
    // tạo user tạm (chưa active)
    public void insertPendingUser(String username, String email, String password,
                                  String otp, LocalDateTime expiredAt) {
        getJdbi().withHandle(h ->
                h.createUpdate(
                                "INSERT INTO users(username,email,password,otp_code,otp_expired_at,is_active,status) " +
                                        "VALUES (:u,:e,:p,:otp,:exp,0,'PENDING')"
                        )
                        .bind("u", username)
                        .bind("e", email)
                        .bind("p", password)
                        .bind("otp", otp)
                        .bind("exp", expiredAt)
                        .execute()
        );
    }

    // verify OTP
    public boolean verifyOtp(String email, String otp) {
        return getJdbi().withHandle(h ->
                h.createUpdate(
                                "UPDATE users SET is_active=1, status = 'ACTIVE',otp_code=NULL, otp_expired_at=NULL " +
                                        "WHERE email=:e AND otp_code=:otp AND otp_expired_at > NOW()"
                        )
                        .bind("e", email)
                        .bind("otp", otp)
                        .execute()
        ) > 0;
    }
    public void updateOtp(String email, String otp, LocalDateTime expiredAt) {
        getJdbi().withHandle(h ->
                h.createUpdate(
                                "UPDATE users SET otp_code=:otp, otp_expired_at=:exp " +
                                        "WHERE email=:e AND is_active=0"
                        )
                        .bind("otp", otp)
                        .bind("exp", expiredAt)
                        .bind("e", email)
                        .execute()
        );
    }
    public boolean verifyOtpForReset(String email, String otp) {
        int count = getJdbi().withHandle(h ->
                h.createQuery(
                        "SELECT COUNT(*) FROM users " +
                                "WHERE email=:e AND otp_code=:otp AND otp_expired_at > NOW()").bind("e", email).bind("otp", otp).mapTo(int.class).one());

        System.out.println("VERIFY RESET COUNT = " + count);
        return count > 0;
    }
    public void updatePassword(String email, String password) {
        getJdbi().withHandle(h ->
                h.createUpdate("UPDATE users SET password=:p, otp_code=NULL, otp_expired_at=NULL " +
                        "WHERE email=:e").bind("p", password).bind("e", email).execute()
        );
    }
    public void updateOtpForReset(String email, String otp, LocalDateTime expiredAt) {
        getJdbi().withHandle(h ->
                h.createUpdate("UPDATE users SET otp_code=:otp, otp_expired_at=:exp WHERE email=:e").bind("otp", otp).bind("exp", expiredAt).bind("e", email).execute());
    }
    public boolean checkOtpCuoi(String email, String otp) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM users " +
                        "WHERE email=:e AND otp_code=:otp AND otp_expired_at > NOW()").bind("e", email).bind("otp", otp).mapTo(int.class).one()) > 0;
    }


    public User findUserById(int id) {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT id,
                       username,
                       email,
                       role,
                       is_active,
                       created_at,
                       full_name,
                       birthday,
                       gender,
                       phone,
                       address,
                       status
                FROM users
                WHERE id = :id
            """)
                        .bind("id", id)
                        .mapToBean(User.class)
                        .findOne()
                        .orElse(null)
        );
    }

    public void update(User user) {
        getJdbi().withHandle(handle -> handle.createUpdate("""
                        UPDATE users
                                    SET full_name = :fullName,
                                        phone = :phone,
                                        email = :email,
                                        birthday = :birthday,
                                        gender = :gender,
                                        address = :address
                                    WHERE id = :id
                        """)
                .bind("fullName", user.getFullName())
                .bind("phone", user.getPhone())
                .bind("email", user.getEmail())
                .bind("birthday", user.getBirthday()) // LocalDate OK
                .bind("gender", user.getGender())
                .bind("address", user.getAddress())
                .bind("id", user.getId())
                .execute()
        );

    }


    public String getPasswordById(int id) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT password
                FROM users
                WHERE id = :id""")
                .bind("id", id)
                .mapTo(String.class)
                .findOne()
                .orElse(null));
    }

    public boolean updatePasss(int id, String hash) {
        return  getJdbi().withHandle(handle -> handle.createUpdate("""
                UPDATE users
                SET password = :hash
                WHERE id = :id""")
                .bind("hash",hash)
                .bind("id", id)
                .execute()) > 0;        //Nếu update thành công thì trả về 1 không thì trả về 0
    }

    public int getCountInWeek() {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT COUNT(*)
                FROM users
                WHERE created_at >= NOW() - INTERVAL 7 DAY
                """)
                .mapTo(int.class)
                .findOne()
                .orElse(0));
    }

    public int getCountActive() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT COUNT(*)
                FROM users
                WHERE status = 'ACTIVE'
            """)
                        .mapTo(int.class)
                        .one()
        );
    }


    public int getCountBlock() {
        return getJdbi().withHandle(handle ->
                handle.createQuery("""
                SELECT COUNT(*)
                FROM users
                WHERE status = 'BLOCKED'
            """)
                        .mapTo(int.class)
                        .one()
        );
    }

    public List<User> searchByUsernameOrEmail(String keyword) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM users
                WHERE username LIKE :keyword OR email LIKE :keyword
                """).bind("keyword", "%" + keyword + "%")
                .mapToBean(User.class)
                .list());
    }

    public void createUser(User user) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                INSERT INTO users
                (username, email, password, role, status,
                 full_name, birthday, gender, phone, address, created_at)
                VALUES
                (:username, :email, :password, :role, :status,
                 :fullName, :birthday, :gender, :phone, :address, NOW())
            """)
                        .bindBean(user)
                        .execute()
        );
    }


    public void updateUser(User user) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                UPDATE users
                SET username = :username,
                    email = :email,
                    role = :role,
                    status = :status,
                    full_name = :fullName,
                    phone = :phone,
                    birthday = :birthday,
                    gender = :gender,
                    address = :address
                WHERE id = :id
            """)
                        .bindBean(user)
                        .execute()
        );
    }



    public void blockUser(int id, String status) {
        getJdbi().withHandle(handle ->
                handle.createUpdate("""
                UPDATE users
                SET status = :status
                WHERE id = :id
            """)
                        .bind("status", status)
                        .bind("id", id)
                        .execute()
        );
    }
    public boolean existsByUsername(String username) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM users WHERE username = :u")
                        .bind("u", username)
                        .mapTo(int.class)
                        .one()
        ) > 0;
    }

    public boolean existsByEmail(String email) {
        return getJdbi().withHandle(h ->
                h.createQuery("SELECT COUNT(*) FROM users WHERE email = :e")
                        .bind("e", email)
                        .mapTo(int.class)
                        .one()
        ) > 0;
    }

    public User findByEmail(String email) {
        return getJdbi().withHandle(handle -> handle.createQuery("""
                SELECT *
                FROM users
                WHERE email = :email
                LIMIT 1
                """)
                .bind("email", email)
                .registerRowMapper(BeanMapper.factory(User.class))
                .mapTo(User.class)
                .findOne()
                .orElse(null));
    }

    public int createGoogleUser(User user) {
        return  getJdbi().withHandle(handle -> handle.createUpdate("""
                INSERT INTO users (email, full_name, role, status, is_active)
                VALUES (:email, :fullName, :role, :status, :isActive)
                """)
                .bind("email", user.getEmail())
                .bind("fullName", user.getFullName())
                .bind("role", user.getRole())
                .bind("status", user.getStatus())
                .bind("isActive", user.getIsActive())
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Integer.class)
                .one());
    }
}

