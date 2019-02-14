package pri.zhong.springboot.redis.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class Counter {

    private StringRedisTemplate template;
    private BoundValueOperations<String, String> count;

    @Autowired
    public Counter(StringRedisTemplate template) {
        this.template = template;
        count = this.template.boundValueOps("count");
    }

    public void add() {
        count.increment();
    }

    public String get() {
        return count.get();
    }
}
