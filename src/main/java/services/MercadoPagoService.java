package services;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.MercadoPagoClient;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;
import controllers.AlumnosController;
import controllers.CursosController;
import controllers.MateriasController;
import impl.Alumno;
import impl.Curso;
import impl.Materia;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.UUID;

public class MercadoPagoService {
    private static MercadoPagoService instance = new MercadoPagoService();

    private MercadoPagoService() {
        String accessToken = "TEST-6293785941812038-051902-4880a4dbbe9e1c5456bb34ecf0cb6341-541675634";
        MercadoPagoConfig.setAccessToken(accessToken);
    }

    public static MercadoPagoService getInstance() {
        return instance;
    }

    public String crearLinkDePago(String title, String description, double unitPrice) {
        UUID idPago = UUID.randomUUID();

        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .id(String.valueOf(idPago))
                .title(description)
                .description(title)
                .quantity(1)
                .currencyId("ARS")
                .unitPrice(BigDecimal.valueOf(unitPrice))
                .build();

        ArrayList<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);

        PreferenceRequest preferenceRequest = PreferenceRequest.builder().items(items).build();
        PreferenceClient client = new PreferenceClient();

        try {
            return client.create(preferenceRequest).getInitPoint();
        } catch (MPApiException e) {
            System.err.println("API Error:");
            System.err.println("Status: " + e.getStatusCode());
            System.err.println("Response: " + e.getApiResponse().getContent());
            e.printStackTrace();
            return null;
        } catch (MPException e) {
            e.printStackTrace();
            return null;
        }
    }
}
