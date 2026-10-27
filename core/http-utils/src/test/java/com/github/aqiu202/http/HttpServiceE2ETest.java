package com.github.aqiu202.http;

import com.github.aqiu202.http.exchange.ApacheHttpClientExchanger;
import com.github.aqiu202.http.exchange.HttpExchanger;
import org.junit.jupiter.api.Named;

import java.util.stream.Stream;

/**
 * Apache 默认、独立和有状态实例运行公共 HTTP 契约。
 */
class HttpServiceE2ETest extends HttpServiceContract {

    @Override
    protected Stream<Named<HttpExchanger>> exchangers() {
        return Stream.of(
                Named.of("ApacheDefault", ApacheHttpClientExchanger.INSTANCE),
                Named.of("Apache", ApacheHttpClientExchanger.create()));
    }

    @Override
    protected Stream<Named<HttpExchanger>> statefulExchangers() {
        return Stream.of(Named.of("StatefulApache", ApacheHttpClientExchanger.stateful()));
    }
}
