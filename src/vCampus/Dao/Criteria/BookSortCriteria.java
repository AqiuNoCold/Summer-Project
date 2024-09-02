package vCampus.Dao.Criteria;

public class BookSortCriteria extends SortCriteria {
    @Override
    public boolean isValidCriteria(String criterion) {
        switch (criterion) {
            case "copy_count":
            case "review_count":
            case "average_rating":
            case "favorite_count":
            case "borrow_count":
                return true;
            default:
                return false;
        }
    }
}