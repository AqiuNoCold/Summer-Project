package vCampus.Dao.Criteria;

public class BorrowRecordSortCriteria extends SortCriteria {
    @Override
    public boolean isValidCriteria(String criterion) {
        switch (criterion) {
            case "borrow_date":
            case "return_date":
                return true;
            default:
                return false;
        }
    }
}