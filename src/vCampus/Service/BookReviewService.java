package vCampus.Service;

import java.util.ArrayList;
import java.util.List;

import vCampus.Dao.Books.BookReviewDao;
import vCampus.Entity.Books.BookReview;

public class BookReviewService {

    // 根据书评ID列表获取书评对象列表
    public List<BookReview> getBookReviewsByIds(List<String> reviewIds) {
        List<BookReview> reviews = new ArrayList<>();
        BookReviewDao reviewDao = new BookReviewDao();

        for (String reviewId : reviewIds) {
            BookReview review = reviewDao.find(reviewId);
            if (review != null) {
                reviews.add(review);
            }
        }

        return reviews;
    }
}