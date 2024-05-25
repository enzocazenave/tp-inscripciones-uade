package services;
import okhttp3.*;
import java.io.IOException;

public class BinanceService {
    private static BinanceService instance = new BinanceService();

    private BinanceService() {}

    public static BinanceService getInstance() {
        return instance;
    }
}