package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.SizeDao;
import vn.edu.nlu.fit.thuctapltw.model.Size;

import java.util.List;

public class SizeService {
    private final SizeDao sizeDao = new SizeDao();


    public List<Size> getSizeByProductId(int id) {
        return sizeDao.getSizeByProductId(id);
    }
}
