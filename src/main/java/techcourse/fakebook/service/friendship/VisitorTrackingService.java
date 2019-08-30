package techcourse.fakebook.service.friendship;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VisitorTrackingService {
    private static final Logger log = LoggerFactory.getLogger(VisitorTrackingService.class);

    private final Map<Long, Counter<Long>> counters = new HashMap<>();

    public void visit(Long userId, Long visitorId) {
        log.debug("begin");

        Counter visitorCounter = getOrNewInstance(userId);
        visitorCounter.increase(visitorId);

        counters.put(userId, visitorCounter);
    }

    public Counter<Long> currentCounter(Long userId) {
        log.debug("begin");

        log.debug("userId :{}", userId);
        Counter counter = getOrNewInstance(userId);

        return Counter.copyOf(counter);
    }

    private Counter getOrNewInstance(Long userId) {
        return counters.getOrDefault(userId, Counter.newInstance());
    }
}
