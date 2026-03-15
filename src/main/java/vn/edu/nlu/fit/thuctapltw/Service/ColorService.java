package vn.edu.nlu.fit.thuctapltw.Service;

import vn.edu.nlu.fit.thuctapltw.DAO.ColorDao;
import vn.edu.nlu.fit.thuctapltw.model.Color;

import java.util.List;

public class ColorService {
    private final ColorDao colorDao = new ColorDao();

    public List<Color> getColorByProductId(int id) {
        return  colorDao.getColorByProductId(id);
    }
}
