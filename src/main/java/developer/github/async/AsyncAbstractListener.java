package developer.github.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;

/**
 * @package: developer.github.async
 * @class:
 * @author: 黄鹏
 * @date: 2016年08月10日 下午10:50
 * @description:
 */
public class AsyncAbstractListener {

    @Autowired
    @Qualifier("register")
    protected AsyncRegister register;

    @PostConstruct
    public final void init() {
        register.register(this);
    }
}
