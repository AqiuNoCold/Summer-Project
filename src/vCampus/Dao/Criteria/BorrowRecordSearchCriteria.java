package vCampus.Dao.Criteria;

public class BorrowRecordSearchCriteria extends SearchCriteria {
    private boolean includeDeleted = false;

    @Override
    public boolean isValidCriteria(String key) {
        switch (key) {
            case "user_id":
            case "status":
            case "book_id":
                return true;
            default:
                return false;
        }
    }

    public void setIncludeDeleted(boolean includeDeleted) {
        this.includeDeleted = includeDeleted;
    }

    public boolean isIncludeDeleted() {
        return includeDeleted;
    }
}