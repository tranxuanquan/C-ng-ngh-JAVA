package vn.edu.eaut.lab7.util;

import java.util.Collections;
import java.util.List;

public class PaginationUtil {
    public static final int PAGE_SIZE = 5;

    public static <T> List<T> paginate(List<T> all, int page) {
        if (all == null || all.isEmpty()) {
            return Collections.emptyList();
        }
        int safePage = Math.max(page, 1);
        int from = (safePage - 1) * PAGE_SIZE;
        if (from >= all.size()) {
            return Collections.emptyList();
        }
        return all.subList(from, Math.min(from + PAGE_SIZE, all.size()));
    }

    public static int totalPages(int totalItems) {
        if (totalItems <= 0) {
            return 1;
        }
        return (int) Math.ceil((double) totalItems / PAGE_SIZE);
    }
}
