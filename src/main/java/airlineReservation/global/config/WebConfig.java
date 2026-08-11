package airlineReservation.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // すべての API アドレスに対して
                .allowedOrigins("http://localhost:5173") // Vue.js ローカルアドレスを許可
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 許可する HTTP メソッド
                .allowedHeaders("*") // グローバル設定では Spring が内部でパターン処理するため * が使用可能
                .allowCredentials(true); // Cookie / 認証を許可
    }
}
