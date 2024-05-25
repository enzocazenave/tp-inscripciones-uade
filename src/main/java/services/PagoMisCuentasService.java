package services;


public class PagoMisCuentasService {
    private static PagoMisCuentasService instance = new PagoMisCuentasService();

    private PagoMisCuentasService() {}

    public static PagoMisCuentasService getInstance() {
        return instance;
    }
}