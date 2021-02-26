package link.dayang.tjutname.datasource;

import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class KeBiaoFetcher {

    private static final OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)  //禁止OkHttp的重定向操作，我们自己处理重定向
            .build();

    private static String COOKIE_IPLANETDIRECTORYPRO_JSESSIONID;
    private static String COOKIE_IPLANETDIRECTORYPRO;

    //semester      格式                      含义
    //              2020-2021-1             2020年9月秋冬季课表
    //              2020-2021-2             2021年3月春夏季课表
    @Nullable
    public static String getRawKeBiao(String id, String password, String semester) {
        Request request = new Request.Builder()
                .url("http://authserver.tjut.edu.cn/authserver/login?service=http%3A%2F%2Fssfw.tjut.edu.cn%2Fssfw%2Fcas_index.jsp")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String body;
            String cookie;
            String COOKIE_ROUTE, COOKIE_JSESSIONID;
            body = Objects.requireNonNull(response.body()).string();
            List<String> headers = response.headers("Set-Cookie");
            COOKIE_ROUTE = headers.get(0);
            COOKIE_JSESSIONID = headers.get(1).split(";", 2)[0];
            cookie = COOKIE_ROUTE + "; " + COOKIE_JSESSIONID;
            String[] ltSplit = body.split("name=\"lt\" value=\"", 2)[1].split("\"", 2);
            String[] dlltSplit = ltSplit[1].split("name=\"dllt\" value=\"", 2)[1].split("\"", 2);
            String[] executionSplit = dlltSplit[1].split("name=\"execution\" value=\"", 2)[1].split("\"", 2);
            String[] _eventIdSplit = executionSplit[1].split("name=\"_eventId\" value=\"", 2)[1].split("\"", 2);
            String[] rmShownSplit = _eventIdSplit[1].split("name=\"rmShown\" value=\"", 2)[1].split("\"", 2);
            String lt = ltSplit[0];
            String dllt = dlltSplit[0];
            String execution = executionSplit[0];
            String _eventId = _eventIdSplit[0];
            String rmShown = rmShownSplit[0];
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody Body = RequestBody.create("username=" + id + "&password=" + password + "&lt=" + lt + "&dllt=" + dllt + "&execution=" + execution + "&_eventId=" + _eventId + "&rmShown=" + rmShown, mediaType);
            request = new Request.Builder()
                    .url("http://authserver.tjut.edu.cn/authserver/login;jsessionid=" + cookie.split("JSESSIONID=", 2)[1] + "?service=http%3A%2F%2Fssfw.tjut.edu.cn%2Fssfw%2Fcas_index.jsp")
                    .post(Body)
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Cookie", cookie)
                    .addHeader("Host", "authserver.tjut.edu.cn")
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Origin", "http://authserver.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/authserver/login?service=http%3A%2F%2Fssfw.tjut.edu.cn%2Fssfw%2Fcas_index.jsp")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            headers = response.headers("Set-Cookie");
            String COOKIE_CASTGC = headers.get(0).split(";", 2)[0];
            String COOKIE_CASPRIVACY = headers.get(1).split(";", 2)[0];
            COOKIE_IPLANETDIRECTORYPRO = headers.get(2).split(";", 2)[0];
            String URL = response.header("Location");
            assert URL != null;
            request = new Request.Builder()
                    .url(URL)
                    .addHeader("Cookie", COOKIE_IPLANETDIRECTORYPRO)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Host", "ssfw.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            URL = response.header("Location");
            COOKIE_IPLANETDIRECTORYPRO_JSESSIONID = Objects.requireNonNull(response.header("Set-Cookie")).split(";", 2)[0];
            assert URL != null;
            request = new Request.Builder()
                    .url(URL)
                    .addHeader("Cookie", COOKIE_IPLANETDIRECTORYPRO + "; " + COOKIE_IPLANETDIRECTORYPRO_JSESSIONID)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Host", "ssfw.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            URL = response.header("Location");
            assert URL != null;
            request = new Request.Builder()
                    .url(URL)
                    .addHeader("Cookie", COOKIE_CASTGC + "; " + COOKIE_ROUTE + "; " + COOKIE_JSESSIONID + "; " + COOKIE_IPLANETDIRECTORYPRO)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Host", "authserver.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            URL = response.header("Location");
            assert URL != null;
            request = new Request.Builder()
                    .url(URL)
                    .addHeader("Cookie", COOKIE_IPLANETDIRECTORYPRO_JSESSIONID + "; " + COOKIE_IPLANETDIRECTORYPRO)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Host", "ssfw.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            URL = response.header("Location");
            assert URL != null;
            request = new Request.Builder()
                    .url(URL)
                    .addHeader("Cookie", COOKIE_IPLANETDIRECTORYPRO_JSESSIONID + "; " + COOKIE_IPLANETDIRECTORYPRO)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Host", "ssfw.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            client.newCall(request).execute();

            //上面为登录必须步骤

            /*
            登录获取的COOKIE
            COOKIE_ROUTE;
            COOKIE_CASTGC;
            COOKIE_CASPRIVACY;
            COOKIE_JSESSIONID;
            COOKIE_IPLANETDIRECTORYPRO;
            COOKIE_IPLANETDIRECTORYPRO_JSESSIONID;
            */

            //这是获取课表
            URL = "http://ssfw.tjut.edu.cn/ssfw/pkgl/kcbxx/4/" + semester + ".do";
            request = new Request.Builder()
                    .url(URL)
                    .addHeader("Cookie", COOKIE_IPLANETDIRECTORYPRO_JSESSIONID + "; " + COOKIE_IPLANETDIRECTORYPRO)
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .addHeader("Accept-Encoding", "gzip, deflate")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Host", "ssfw.tjut.edu.cn")
                    .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                    .addHeader("Upgrade-Insecure-Requests", "1")
                    .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                    .build();
            response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static String fetchJbxx() {
        String url = "http://ssfw.tjut.edu.cn/ssfw/xjgl/jbxx.do";
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie", COOKIE_IPLANETDIRECTORYPRO_JSESSIONID + "; " + COOKIE_IPLANETDIRECTORYPRO)
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Connection", "keep-alive")
                .addHeader("Host", "ssfw.tjut.edu.cn")
                .addHeader("Referer", "http://authserver.tjut.edu.cn/")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36")
                .build();
        try {
            Response response = client.newCall(request).execute();
            return Objects.requireNonNull(response.body()).string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
