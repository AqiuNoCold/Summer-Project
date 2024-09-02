package vCampus.Dao.Criteria;

import java.util.HashMap;
import java.util.Map;

public abstract class SearchCriteria {
    protected Map<String, String> criteria = new HashMap<>();

    public void addCriteria(String key, String value) {
        criteria.put(key, value);
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    public abstract boolean isValidCriteria(String key);
}