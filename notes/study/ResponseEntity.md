# HttpEntity<T>
- package
  - `org.springframework.http`
- Type Parameters
  - `T` - the body type
- 코드
```java
package org.springframework.http;

import org.springframework.lang.Nullable;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;

public class HttpEntity<T> {
    public static final HttpEntity<?> EMPTY = new HttpEntity();
    private final HttpHeaders headers;
    @Nullable
    private final T body;

    protected HttpEntity() {
        this((Object)null, (MultiValueMap)null);
    }

    public HttpEntity(T body) {
        this(body, (MultiValueMap)null);
    }

    public HttpEntity(MultiValueMap<String, String> headers) {
        this((Object)null, headers);
    }

    public HttpEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers) {
        this.body = body;
        this.headers = HttpHeaders.readOnlyHttpHeaders((MultiValueMap)(headers != null ? headers : new HttpHeaders()));
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    @Nullable
    public T getBody() {
        return this.body;
    }

    public boolean hasBody() {
        return this.body != null;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (other != null && other.getClass() == this.getClass()) {
            HttpEntity<?> otherEntity = (HttpEntity)other;
            return ObjectUtils.nullSafeEquals(this.headers, otherEntity.headers) && ObjectUtils.nullSafeEquals(this.body, otherEntity.body);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.headers) * 29 + ObjectUtils.nullSafeHashCode(this.body);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        if (this.body != null) {
            builder.append(this.body);
            builder.append(',');
        }

        builder.append(this.headers);
        builder.append('>');
        return builder.toString();
    }
}
```
- 헤더와 본문으로 구성된 HTTP 요청 또는 응답 엔터티를 나타냅니다. 다음과 같이 RestTemplate와 종종 함께 사용됩니다.
```java
HttpHeaders 헤더 = 새로운 HttpHeaders();
headers.setContentType(MediaType.TEXT_PLAIN);

HttpEntity<String> 엔터티 = new HttpEntity<>("Hello World", 헤더);
URI 위치 = template.postForLocation("https://example.com", 엔터티);
```

또는

```java
HttpEntity<String> 엔터티 = template.getForEntity("https://example.com", String.class);

문자열 본문 = 엔터티.getBody();
MediaType contentType = 엔터티.getHeaders().getContentType();
```

**@Controller 메서드의 반환 값으로 Spring MVC에서 사용할 수도 있습니다.**
```java
@GetMapping("/핸들")
공개 HttpEntity<String> 핸들() {
HttpHeaders responseHeaders = 새로운 HttpHeaders();

responseHeaders.set("MyResponseHeader", "MyValue");
새로운 HttpEntity<>("Hello World", responseHeaders)를 반환합니다.
}
```

## 필드 요약
| 수정자 및 유형                   | 필드    | 설명                        |
|----------------------------|-------|---------------------------|
| static final HttpEntity<?> | EMPTY | 본문과 헤더 없이 비어있는 HttpEntity |


## 생성자 요약
| 수정자       | 생성자                                                      | 설명              |
|-----------|----------------------------------------------------------|-----------------|
| protected | HttpEntity()                                             | 비어있는 HttpEntity |
| public    | HttpEntity(MultiValueMap<String, String> headers         | 헤더 O, 바디 X      |
| public    | HttpEntity(T body)                                       | 헤더 X, 바디 O      |
| public    | HttpEntity(T body, MultiValueMap<String, String> headers | 헤더 O, 바디 O      |

# RequestEntity<T>
```java
public class RequestEntity<T> extends HttpEntity<T> {
    @Nullable
    private final HttpMethod method;
    @Nullable
    private final URI url;
    @Nullable
    private final Type type;

    public RequestEntity(HttpMethod method, URI url) {
        this((Object)null, (MultiValueMap)null, method, url, (Type)null);
    }

    public RequestEntity(@Nullable T body, HttpMethod method, URI url) {
        this(body, (MultiValueMap)null, method, url, (Type)null);
    }

    public RequestEntity(@Nullable T body, HttpMethod method, URI url, Type type) {
        this(body, (MultiValueMap)null, method, url, type);
    }

    public RequestEntity(MultiValueMap<String, String> headers, HttpMethod method, URI url) {
        this((Object)null, headers, method, url, (Type)null);
    }

    public RequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, @Nullable HttpMethod method, URI url) {
        this(body, headers, method, url, (Type)null);
    }

    public RequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, @Nullable HttpMethod method, @Nullable URI url, @Nullable Type type) {
        super(body, headers);
        this.method = method;
        this.url = url;
        this.type = type;
    }

    @Nullable
    public HttpMethod getMethod() {
        return this.method;
    }

    public URI getUrl() {
        if (this.url == null) {
            throw new UnsupportedOperationException();
        } else {
            return this.url;
        }
    }

    @Nullable
    public Type getType() {
        if (this.type == null) {
            T body = this.getBody();
            if (body != null) {
                return body.getClass();
            }
        }

        return this.type;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!super.equals(other)) {
            return false;
        } else {
            RequestEntity<?> otherEntity = (RequestEntity)other;
            return ObjectUtils.nullSafeEquals(this.getMethod(), otherEntity.getMethod()) && ObjectUtils.nullSafeEquals(this.getUrl(), otherEntity.getUrl());
        }
    }

    public int hashCode() {
        int hashCode = super.hashCode();
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.method);
        hashCode = 29 * hashCode + ObjectUtils.nullSafeHashCode(this.getUrl());
        return hashCode;
    }

    public String toString() {
        return format(this.getMethod(), this.getUrl().toString(), this.getBody(), this.getHeaders());
    }

    static <T> String format(@Nullable HttpMethod httpMethod, String url, @Nullable T body, HttpHeaders headers) {
        StringBuilder builder = new StringBuilder("<");
        builder.append(httpMethod);
        builder.append(' ');
        builder.append(url);
        builder.append(',');
        if (body != null) {
            builder.append(body);
            builder.append(',');
        }

        builder.append(headers);
        builder.append('>');
        return builder.toString();
    }

    public static BodyBuilder method(HttpMethod method, URI url) {
        return new DefaultBodyBuilder(method, url);
    }

    public static BodyBuilder method(HttpMethod method, String uriTemplate, Object... uriVariables) {
        return new DefaultBodyBuilder(method, uriTemplate, uriVariables);
    }

    public static BodyBuilder method(HttpMethod method, String uriTemplate, Map<String, ?> uriVariables) {
        return new DefaultBodyBuilder(method, uriTemplate, uriVariables);
    }

    public static HeadersBuilder<?> get(URI url) {
        return method(HttpMethod.GET, url);
    }

    public static HeadersBuilder<?> get(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.GET, uriTemplate, uriVariables);
    }

    public static HeadersBuilder<?> head(URI url) {
        return method(HttpMethod.HEAD, url);
    }

    public static HeadersBuilder<?> head(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.HEAD, uriTemplate, uriVariables);
    }

    public static BodyBuilder post(URI url) {
        return method(HttpMethod.POST, url);
    }

    public static BodyBuilder post(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.POST, uriTemplate, uriVariables);
    }

    public static BodyBuilder put(URI url) {
        return method(HttpMethod.PUT, url);
    }

    public static BodyBuilder put(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.PUT, uriTemplate, uriVariables);
    }

    public static BodyBuilder patch(URI url) {
        return method(HttpMethod.PATCH, url);
    }

    public static BodyBuilder patch(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.PATCH, uriTemplate, uriVariables);
    }

    public static HeadersBuilder<?> delete(URI url) {
        return method(HttpMethod.DELETE, url);
    }

    public static HeadersBuilder<?> delete(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.DELETE, uriTemplate, uriVariables);
    }

    public static HeadersBuilder<?> options(URI url) {
        return method(HttpMethod.OPTIONS, url);
    }

    public static HeadersBuilder<?> options(String uriTemplate, Object... uriVariables) {
        return method(HttpMethod.OPTIONS, uriTemplate, uriVariables);
    }

    public static class UriTemplateRequestEntity<T> extends RequestEntity<T> {
        private final String uriTemplate;
        @Nullable
        private final Object[] uriVarsArray;
        @Nullable
        private final Map<String, ?> uriVarsMap;

        UriTemplateRequestEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, @Nullable HttpMethod method, @Nullable Type type, String uriTemplate, @Nullable Object[] uriVarsArray, @Nullable Map<String, ?> uriVarsMap) {
            super(body, headers, method, (URI)null, type);
            this.uriTemplate = uriTemplate;
            this.uriVarsArray = uriVarsArray;
            this.uriVarsMap = uriVarsMap;
        }

        public String getUriTemplate() {
            return this.uriTemplate;
        }

        @Nullable
        public Object[] getVars() {
            return this.uriVarsArray;
        }

        @Nullable
        public Map<String, ?> getVarsMap() {
            return this.uriVarsMap;
        }

        public String toString() {
            return format(this.getMethod(), this.getUriTemplate(), this.getBody(), this.getHeaders());
        }
    }

    private static class DefaultBodyBuilder implements BodyBuilder {
        private final HttpMethod method;
        private final HttpHeaders headers = new HttpHeaders();
        @Nullable
        private final URI uri;
        @Nullable
        String uriTemplate;
        @Nullable
        private Object[] uriVarsArray;
        @Nullable
        Map<String, ?> uriVarsMap;

        DefaultBodyBuilder(HttpMethod method, URI url) {
            this.method = method;
            this.uri = url;
            this.uriTemplate = null;
            this.uriVarsArray = null;
            this.uriVarsMap = null;
        }

        DefaultBodyBuilder(HttpMethod method, String uriTemplate, Object... uriVars) {
            this.method = method;
            this.uri = null;
            this.uriTemplate = uriTemplate;
            this.uriVarsArray = uriVars;
            this.uriVarsMap = null;
        }

        DefaultBodyBuilder(HttpMethod method, String uriTemplate, Map<String, ?> uriVars) {
            this.method = method;
            this.uri = null;
            this.uriTemplate = uriTemplate;
            this.uriVarsArray = null;
            this.uriVarsMap = uriVars;
        }

        public BodyBuilder header(String headerName, String... headerValues) {
            String[] var3 = headerValues;
            int var4 = headerValues.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String headerValue = var3[var5];
                this.headers.add(headerName, headerValue);
            }

            return this;
        }

        public BodyBuilder headers(@Nullable HttpHeaders headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }

            return this;
        }

        public BodyBuilder headers(Consumer<HttpHeaders> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        public BodyBuilder accept(MediaType... acceptableMediaTypes) {
            this.headers.setAccept(Arrays.asList(acceptableMediaTypes));
            return this;
        }

        public BodyBuilder acceptCharset(Charset... acceptableCharsets) {
            this.headers.setAcceptCharset(Arrays.asList(acceptableCharsets));
            return this;
        }

        public BodyBuilder contentLength(long contentLength) {
            this.headers.setContentLength(contentLength);
            return this;
        }

        public BodyBuilder contentType(MediaType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        public BodyBuilder ifModifiedSince(ZonedDateTime ifModifiedSince) {
            this.headers.setIfModifiedSince(ifModifiedSince);
            return this;
        }

        public BodyBuilder ifModifiedSince(Instant ifModifiedSince) {
            this.headers.setIfModifiedSince(ifModifiedSince);
            return this;
        }

        public BodyBuilder ifModifiedSince(long ifModifiedSince) {
            this.headers.setIfModifiedSince(ifModifiedSince);
            return this;
        }

        public BodyBuilder ifNoneMatch(String... ifNoneMatches) {
            this.headers.setIfNoneMatch(Arrays.asList(ifNoneMatches));
            return this;
        }

        public RequestEntity<Void> build() {
            return this.buildInternal((Object)null, (Type)null);
        }

        public <T> RequestEntity<T> body(T body) {
            return this.buildInternal(body, (Type)null);
        }

        public <T> RequestEntity<T> body(T body, Type type) {
            return this.buildInternal(body, type);
        }

        private <T> RequestEntity<T> buildInternal(@Nullable T body, @Nullable Type type) {
            if (this.uri != null) {
                return new RequestEntity(body, this.headers, this.method, this.uri, type);
            } else if (this.uriTemplate != null) {
                return new UriTemplateRequestEntity(body, this.headers, this.method, type, this.uriTemplate, this.uriVarsArray, this.uriVarsMap);
            } else {
                throw new IllegalStateException("Neither URI nor URI template");
            }
        }
    }

    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
        BodyBuilder contentLength(long var1);

        BodyBuilder contentType(MediaType var1);

        <T> RequestEntity<T> body(T var1);

        <T> RequestEntity<T> body(T var1, Type var2);
    }

    public interface HeadersBuilder<B extends HeadersBuilder<B>> {
        B header(String var1, String... var2);

        B headers(@Nullable HttpHeaders var1);

        B headers(Consumer<HttpHeaders> var1);

        B accept(MediaType... var1);

        B acceptCharset(Charset... var1);

        B ifModifiedSince(ZonedDateTime var1);

        B ifModifiedSince(Instant var1);

        B ifModifiedSince(long var1);

        B ifNoneMatch(String... var1);

        RequestEntity<Void> build();
    }
}
```

HTTP 메서드와 대상 URL도 노출하는 HttpEntity의 확장입니다.
요청 입력을 나타내기 위해 @Controller 메소드를 사용하여 요청을 준비하기 위해 RestTemplate에서 사용됩니다.
- RestTemplate을 사용한 사용 예:
```java
MyRequest body = ...
RequestEntity<MyRequest> request = RequestEntity
    .post("https://example.com/{foo}", "bar")
    .accept(MediaType.APPLICATION_JSON)
    .body(body);
ResponseEntity<MyResponse> response = template.exchange(request, MyResponse.class);
```

```java
@RequestMapping("/handle")
public void handle(RequestEntity<String> request) {
    HttpMethod method = request.getMethod();
    URI url = request.getUrl();
    String body = request.getBody();
}
```

## Nested Class Summary
| Modifier and Type | Class                                                            | Description                                |
|-------------------|------------------------------------------------------------------|--------------------------------------------|
| static interface  | RequestEntity.BodyBuilder                                        | 응답 엔티티에 본문을 추가                             |
| static interface  | RequestEntity.HeadersBuilder<B extends Request.HeadersBuilder<B> | 요청 엔티티에 헤더를 추가                             |
| static class      | RequestEntity.UriTemplateRequestEntity<T>                        | URI 대신 URI 템플릿과 변수를 사용하여 RequestEntity 초기화 |  

## Constructor Summary
| Constructor                                                                                        | Description                                   |
|----------------------------------------------------------------------------------------------------|-----------------------------------------------|
| RequestEntity<HttpMethod method, URI uri)                                                          | HttpMethod O, URL O, Header X, Body X         |
| RequestEntity(MultiValueMap<String,String> headers, HttpMethod method, URI url)                    | HttpMethod O, URL O, Header O, Body X         |
| RequestEntity(T body, HttpMethod method, URI url)                                                  | HttpMethod O, URL O, Header X, Body O         |
| RequestEntity(T body, HttpMethod method, URI url, Type type)                                       | HttpMethod O, URL O, Header X, Body O, Type O |
| RequestEntity(T body, MultiValueMap<String,String> headers, HttpMethod method, URI url)            | HttpMethod O, URL O, Header O, Body O         |
| RequestEntity(T body, MultiValueMap<String,String> headers, HttpMethod method, URI url, Type type) | HttpMethod O, URL O, Header O, Body O, Type O |

## Method
| 수정자 및 유형  | 방법                                                                          | 설명                                               |
|-----------|-----------------------------------------------------------------------------|--------------------------------------------------|
| delete    | `delete(String uriTemplate, Object... uriVariables)`                        | 주어진 문자열 기본 URI 템플릿을 사용하여 HTTP DELETE 빌더를 만듭니다.   |
|           | `delete(URI url)`                                                           | 주어진 URL을 사용하여 HTTP DELETE 빌더를 만듭니다.              |
| equals    | `boolean equals(Object other)`                                              | 객체의 동일성을 확인합니다.                                  |
| get       | `get(String uriTemplate, Object... uriVariables)`                           | 주어진 문자열 기본 URI 템플릿을 사용하여 HTTP GET 빌더를 만듭니다.      |
|           | `get(URI url)`                                                              | 주어진 URL을 사용하여 HTTP GET 빌더를 만듭니다.                 |
| getMethod | `HttpMethod getMethod()`                                                    | 요청의 HTTP 메소드를 반환합니다.                             |
| getType   | `Type getType()`                                                            | 요청 본문의 유형을 반환합니다.                                |
| getUrl    | `URI getUrl()`                                                              | URI 대상 HTTP 엔드포인트에 대해 URL을 반환합니다.                |
| hashCode  | `int hashCode()`                                                            | 객체의 해시 코드를 반환합니다.                                |
| head      | `head(String uriTemplate, Object... uriVariables)`                          | 주어진 문자열 기본 URI 템플릿을 사용하여 HTTP HEAD 빌더를 만듭니다.     |
|           | `head(URI url)`                                                             | 주어진 URL을 사용하여 HTTP HEAD 빌더를 만듭니다.                |
| method    | `method(HttpMethod method, String uriTemplate, Object... uriVariables)`     | 지정된 HTTP 메서드, URI 템플릿 및 변수를 사용하여 빌더를 만듭니다.       |
|           | `method(HttpMethod method, String uriTemplate, Map<String,?> uriVariables)` | 지정된 HTTP 메서드, URI 템플릿 및 변수를 사용하여 빌더를 만듭니다.       |
|           | `method(HttpMethod method, URI url)`                                        | 주어진 메소드와 URL을 사용하여 빌더를 만듭니다.                     |
| options   | `options(String uriTemplate, Object... uriVariables)`                       | 지정된 문자열 기본 URI 템플릿을 사용하여 HTTP OPTIONS 빌더를 생성합니다. |
|           | `options(URI url)`                                                          | 지정된 URL을 사용하여 HTTP OPTIONS 빌더를 생성합니다.            |
| patch     | `patch(String uriTemplate, Object... uriVariables)`                         | 지정된 문자열 기본 URI 템플릿을 사용하여 HTTP PATCH 빌더를 만듭니다.    |
|           | `patch(URI url)`                                                            | 주어진 URL을 사용하여 HTTP PATCH 빌더를 만듭니다.               |
| post      | `post(String uriTemplate, Object... uriVariables)`                          | 주어진 문자열 기본 URI 템플릿을 사용하여 HTTP POST 빌더를 만듭니다.     |
|           | `post(URI url)`                                                             | 주어진 URL을 사용하여 HTTP POST 빌더를 만듭니다.                |
| put       | `put(String uriTemplate, Object... uriVariables)`                           | 지정된 문자열 기본 URI 템플릿을 사용하여 HTTP PUT 빌더를 만듭니다.      |
|           | `put(URI url)`                                                              | 주어진 URL을 사용하여 HTTP PUT 빌더를 생성합니다.                |
| toString  | `String toString()`                                                         | 객체의 문자열 표현을 반환합니다.                               |

















# ResponseEntity<T>
```java
public class ResponseEntity<T> extends HttpEntity<T> {
    private final Object status;

    public ResponseEntity(HttpStatus status) {
        this((Object)null, (MultiValueMap)null, (HttpStatus)status);
    }

    public ResponseEntity(@Nullable T body, HttpStatus status) {
        this(body, (MultiValueMap)null, (HttpStatus)status);
    }

    public ResponseEntity(MultiValueMap<String, String> headers, HttpStatus status) {
        this((Object)null, headers, (HttpStatus)status);
    }

    public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, HttpStatus status) {
        this(body, headers, (Object)status);
    }

    public ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, int rawStatus) {
        this(body, headers, (Object)rawStatus);
    }

    private ResponseEntity(@Nullable T body, @Nullable MultiValueMap<String, String> headers, Object status) {
        super(body, headers);
        Assert.notNull(status, "HttpStatus must not be null");
        this.status = status;
    }

    public HttpStatus getStatusCode() {
        return this.status instanceof HttpStatus ? (HttpStatus)this.status : HttpStatus.valueOf((Integer)this.status);
    }

    public int getStatusCodeValue() {
        return this.status instanceof HttpStatus ? ((HttpStatus)this.status).value() : (Integer)this.status;
    }

    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        } else if (!super.equals(other)) {
            return false;
        } else {
            ResponseEntity<?> otherEntity = (ResponseEntity)other;
            return ObjectUtils.nullSafeEquals(this.status, otherEntity.status);
        }
    }

    public int hashCode() {
        return 29 * super.hashCode() + ObjectUtils.nullSafeHashCode(this.status);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("<");
        builder.append(this.status);
        if (this.status instanceof HttpStatus) {
            builder.append(' ');
            builder.append(((HttpStatus)this.status).getReasonPhrase());
        }

        builder.append(',');
        T body = this.getBody();
        HttpHeaders headers = this.getHeaders();
        if (body != null) {
            builder.append(body);
            builder.append(',');
        }

        builder.append(headers);
        builder.append('>');
        return builder.toString();
    }

    public static BodyBuilder status(HttpStatus status) {
        Assert.notNull(status, "HttpStatus must not be null");
        return new DefaultBuilder(status);
    }

    public static BodyBuilder status(int status) {
        return new DefaultBuilder(status);
    }

    public static BodyBuilder ok() {
        return status(HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> ok(@Nullable T body) {
        return ok().body(body);
    }

    public static <T> ResponseEntity<T> of(Optional<T> body) {
        Assert.notNull(body, "Body must not be null");
        return (ResponseEntity)body.map(ResponseEntity::ok).orElseGet(() -> {
            return notFound().build();
        });
    }

    public static BodyBuilder created(URI location) {
        return (BodyBuilder)status(HttpStatus.CREATED).location(location);
    }

    public static BodyBuilder accepted() {
        return status(HttpStatus.ACCEPTED);
    }

    public static HeadersBuilder<?> noContent() {
        return status(HttpStatus.NO_CONTENT);
    }

    public static BodyBuilder badRequest() {
        return status(HttpStatus.BAD_REQUEST);
    }

    public static HeadersBuilder<?> notFound() {
        return status(HttpStatus.NOT_FOUND);
    }

    public static BodyBuilder unprocessableEntity() {
        return status(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private static class DefaultBuilder implements BodyBuilder {
        private final Object statusCode;
        private final HttpHeaders headers = new HttpHeaders();

        public DefaultBuilder(Object statusCode) {
            this.statusCode = statusCode;
        }

        public BodyBuilder header(String headerName, String... headerValues) {
            String[] var3 = headerValues;
            int var4 = headerValues.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String headerValue = var3[var5];
                this.headers.add(headerName, headerValue);
            }

            return this;
        }

        public BodyBuilder headers(@Nullable HttpHeaders headers) {
            if (headers != null) {
                this.headers.putAll(headers);
            }

            return this;
        }

        public BodyBuilder headers(Consumer<HttpHeaders> headersConsumer) {
            headersConsumer.accept(this.headers);
            return this;
        }

        public BodyBuilder allow(HttpMethod... allowedMethods) {
            this.headers.setAllow(new LinkedHashSet(Arrays.asList(allowedMethods)));
            return this;
        }

        public BodyBuilder contentLength(long contentLength) {
            this.headers.setContentLength(contentLength);
            return this;
        }

        public BodyBuilder contentType(MediaType contentType) {
            this.headers.setContentType(contentType);
            return this;
        }

        public BodyBuilder eTag(String etag) {
            if (!etag.startsWith("\"") && !etag.startsWith("W/\"")) {
                etag = "\"" + etag;
            }

            if (!etag.endsWith("\"")) {
                etag = etag + "\"";
            }

            this.headers.setETag(etag);
            return this;
        }

        public BodyBuilder lastModified(ZonedDateTime date) {
            this.headers.setLastModified(date);
            return this;
        }

        public BodyBuilder lastModified(Instant date) {
            this.headers.setLastModified(date);
            return this;
        }

        public BodyBuilder lastModified(long date) {
            this.headers.setLastModified(date);
            return this;
        }

        public BodyBuilder location(URI location) {
            this.headers.setLocation(location);
            return this;
        }

        public BodyBuilder cacheControl(CacheControl cacheControl) {
            this.headers.setCacheControl(cacheControl);
            return this;
        }

        public BodyBuilder varyBy(String... requestHeaders) {
            this.headers.setVary(Arrays.asList(requestHeaders));
            return this;
        }

        public <T> ResponseEntity<T> build() {
            return this.body((Object)null);
        }

        public <T> ResponseEntity<T> body(@Nullable T body) {
            return new ResponseEntity(body, this.headers, this.statusCode);
        }
    }

    public interface BodyBuilder extends HeadersBuilder<BodyBuilder> {
        BodyBuilder contentLength(long var1);

        BodyBuilder contentType(MediaType var1);

        <T> ResponseEntity<T> body(@Nullable T var1);
    }

    public interface HeadersBuilder<B extends HeadersBuilder<B>> {
        B header(String var1, String... var2);

        B headers(@Nullable HttpHeaders var1);

        B headers(Consumer<HttpHeaders> var1);

        B allow(HttpMethod... var1);

        B eTag(String var1);

        B lastModified(ZonedDateTime var1);

        B lastModified(Instant var1);

        B lastModified(long var1);

        B location(URI var1);

        B cacheControl(CacheControl var1);

        B varyBy(String... var1);

        <T> ResponseEntity<T> build();
    }
}
```

- HttpStatusCode 상태 코드를 추가하는 HttpEntity의 확장
- RestTemplate 및 @Controller 메서드에서 사용됩니다.
- RestTemplate에서 이 클래스는 getForEntity() 및 exchange()에 의해 반환됩니다.
```java
ResponseEntity<String> entity = template.getForEntity("https://example.com", String.class);
String body = entity.getBody();
MediaType contentType = entity.getHeaders().getContentType();
HttpStatus statusCode = entity.getStatusCode();
```
- ResponseEntity<T>는 Spring MVC의 @Controller의 Return 타입으로 사용될 수 있다.
```java
@RequestMapping("/handle")
public ResponseEntity<String> handle() {
    URI location = ...;
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.setLocation(location);
    responseHeaders.set("MyResponseHeader", "MyValue");
    return new ResponseEntity<String>("Hello World", responseHeaders, HttpStatus.CREATED);
}
```
- 또는 정적 메서드를 통해 액세스할 수 있는 빌더를 사용합니다.
```java
@RequestMapping("/handle")
public ResponseEntity<String> handle() {
    URI location = ...;
    return ResponseEntity.created(location).header("MyResponseHeader", "MyValue").body("Hello World");
}
```

## 생성자
| 생성자                                                                                 | 설명                          |
|-------------------------------------------------------------------------------------|-----------------------------|
| ResponseEntity<HttpStatueCode status                                                | 상태코드 O                      |
| ResponseEntity<MultiValueMap<String, String> headers, HttpStatusCode status         | 상태코드 O, Header O            |
| ResponseEntity(T body, HttpStatusCode status)                                       | 상태코드 O, Body o              |
| ResponseEntity(T body, MultiValueMap<String, String> headers, int rawStatus         | 상태코드 O, Body O, rawStatus O |
| ResponseEntity<T body, MultiValueMap<String, String> headers, HttpStatusCode status | 상태코드 O, Body O, header O    |

## Method
| 함수                                            | 설명                    |
|-----------------------------------------------|-----------------------|
| getStatusCode()                               | return HttpStatusCode |
| public static ResponseEntity.BodyBuilder ok() | 상태가 OK로 설정된 빌더 생성     |
|                                               |                       |
|                                               |                       |
|                                               |                       |