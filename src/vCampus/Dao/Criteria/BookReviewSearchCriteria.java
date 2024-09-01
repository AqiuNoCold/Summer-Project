package vCampus.Dao.Criteria;

import java.util.HashMap;
import java.util.Map;

public class BookReviewSearchCriteria extends SearchCriteria {
    private Map<String, String> criteria = new HashMap<>();
    private int ratingMin = 0;
    private int ratingMax = 5;

    @Override
    public boolean isValidCriteria(String key) {
        switch (key) {
            case "userId":
            case "bookId":
            case "shelfId":
            case "isPublic":
                return true;
            default:
                return false;
        }
    }

    public void setRatingRange(int min, int max) {
        this.ratingMin = min;
        this.ratingMax = max;
    }

    public int getRatingMin() {
        return ratingMin;
    }

    public int getRatingMax() {
        return ratingMax;
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }

    public void addCriteria(String key, String value) {
        if (isValidCriteria(key)) {
            criteria.put(key, value);
        }
    }
}