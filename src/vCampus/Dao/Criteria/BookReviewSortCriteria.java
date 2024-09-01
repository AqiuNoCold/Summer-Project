package vCampus.Dao.Criteria;

public class BookReviewSortCriteria extends SortCriteria {
    @Override
    public boolean isValidCriteria(String criterion) {
        switch (criterion) {
            case "rating":
            case "createTime":
            case "updateTime":
                return true;
            default:
                return false;
        }
    }
}