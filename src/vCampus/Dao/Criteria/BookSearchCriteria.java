package vCampus.Dao.Criteria;

public class BookSearchCriteria extends SearchCriteria {
    @Override
    public boolean isValidCriteria(String key) {
        switch (key) {
            case "isbn":
            case "isbn13":
            case "title":
            case "authors":
            case "language":
            case "subjects":
            case "synopsis":
            case "publisher":
                return true;
            default:
                return false;
        }
    }
}