package com.github.aqiu202.http;

import com.github.aqiu202.http.exchange.HttpExchanger;
import com.github.aqiu202.http.exchange.RestClientExchanger;
import com.github.aqiu202.http.exchange.RestTemplateExchanger;
import com.github.aqiu202.http.exchange.WebClientExchanger;
import org.junit.jupiter.api.Named;

import java.util.stream.Stream;

/**
 * 原三套 Spring 客户端及其有状态变体运行核心公共契约。
 */
class SpringHttpServiceContractTest extends HttpServiceContract {

    @Override
    protected Stream<Named<HttpExchanger>> exchangers() {
        return Stream.of(
                Named.of("RestClient", RestClientExchanger.create()),
                Named.of("RestTemplate", RestTemplateExchanger.create()),
                Named.of("WebClient", WebClientExchanger.create()));
    }

    @Override
    protected Stream<Named<HttpExchanger>> statefulExchangers() {
        return Stream.of(
                Named.of("StatefulRestClient", RestClientExchanger.stateful()),
                Named.of("StatefulRestTemplate", RestTemplateExchanger.stateful()),
                Named.of("StatefulWebClient", WebClientExchanger.stateful()));
    }
}
