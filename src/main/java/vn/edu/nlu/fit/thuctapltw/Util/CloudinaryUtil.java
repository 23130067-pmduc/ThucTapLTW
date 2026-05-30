package vn.edu.nlu.fit.thuctapltw.Util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class CloudinaryUtil {
    private static Cloudinary cloudinary;

    static {
        try {
            Properties props = new Properties();
            props.load(CloudinaryUtil.class.getClassLoader()
                    .getResourceAsStream("cloudinary.properties"));

            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", props.getProperty("cloudinary.cloud_name"),
                    "api_key", props.getProperty("cloudinary.api_key"),
                    "api_secret", props.getProperty("cloudinary.api_secret"),
                    "secure", true
            ));
        } catch (Exception e) {
            throw new RuntimeException("Không thể load cấu hình Cloudinary", e);
        }
    }

    public static String uploadImage(Part filePart, String folder) throws IOException {
        if (filePart == null
                || filePart.getSubmittedFileName() == null
                || filePart.getSubmittedFileName().isBlank()
                || filePart.getSize() == 0) {
            return null;
        }

        byte[] fileBytes;

        try (InputStream inputStream = filePart.getInputStream()) {
            fileBytes = inputStream.readAllBytes();
        }

        Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                "folder", folder,
                "resource_type", "image"
        ));

        return uploadResult.get("secure_url").toString();
    }
}
