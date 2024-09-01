package vCampus.Dao.Criteria;

import java.util.ArrayList;
import java.util.List;

public abstract class SortCriteria {
    public enum SortOrder {
        ASC, DESC
    }

    protected List<String> criteria = new ArrayList<>();

    public void addCriteria(String criterion, SortOrder order) {
        if (isValidCriteria(criterion)) {
            criteria.add(criterion + " " + order.name());
        }
    }

    public List<String> getCriteria() {
        return criteria;
    }

    public abstract boolean isValidCriteria(String criterion);
}