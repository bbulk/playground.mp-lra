package dev.bbulk.mp_lra.game.clients;

import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "http://localhost:8082")
public interface WalletClient {

    @POST
    @Path("booking")
    void bookMoney(MoneyRequest moneyRequest);

}
