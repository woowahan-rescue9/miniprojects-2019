package techcourse.fakebook.service.friendship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter<T> {
    public static final Long START_COUNT = 0L;

    private final Map<T, Long> counter;

    private Counter() {
        counter = new HashMap();
    }

    public Counter(Map<T, Long> counter) {
        this.counter = counter;
    }

    // shallow copy 임.
    // 현재 사용할 곳들을 생각했을 경우 shallow copy 도 괜찮은 듯 하다. (counting 된 횟수가 달라져도 큰 영향이 없기 때문)
    public static Counter copyOf(Counter counter) {
        return new Counter(new HashMap(counter.counter));
    }

    public static <E> Counter<E> from(Map<E, Long> counter) {
        return new Counter(counter);
    }

    public static Counter<Long> newInstance() {
        return new Counter();
    }

    public void increase(T key) {
        Long count = counter.getOrDefault(key, START_COUNT);

        counter.put(key, count + 1L);
    }

    public Long count(T key) {
        return counter.getOrDefault(key, START_COUNT);
    }

    public Set<T> keySet() {
        return counter.keySet();
    }
}
