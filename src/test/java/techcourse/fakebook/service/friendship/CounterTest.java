package techcourse.fakebook.service.friendship;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class CounterTest {

    @Test
    void copyOf() {
        // 다른 reference 인지
        // 내용은 같은지
    }

    @Test
    void increase_존재하지않는_key() {
        Counter<Long> counter = Counter.newInstance();
        Long key = 0L;

        counter.increase(key);

        assertThat(counter.count(key)).isEqualTo(Counter.START_COUNT + 1);
    }

    @Test
    void count_존재하지않는_key() {
        Counter<Long> counter = Counter.newInstance();
        Long key = 0L;

        assertThat(counter.count(key)).isEqualTo(Counter.START_COUNT);
    }

    @Test
    void 특정수만큼_증가_이후에_세보기() {
        Counter<Long> counter = Counter.newInstance();
        Long key = 0L;
        int numIncreased = 10;
        IntStream.range(0, numIncreased)
                .forEach(num -> counter.increase(key));

        assertThat(counter.count(key)).isEqualTo(Counter.START_COUNT + numIncreased);
    }
}