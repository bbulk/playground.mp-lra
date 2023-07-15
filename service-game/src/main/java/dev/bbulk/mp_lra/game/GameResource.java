package dev.bbulk.mp_lra.game;

import dev.bbulk.mp_lra.game.clients.HistoryClient;
import dev.bbulk.mp_lra.game.clients.HistoryRequest;
import dev.bbulk.mp_lra.game.clients.MoneyRequest;
import dev.bbulk.mp_lra.game.clients.WalletClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static jakarta.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static jakarta.ws.rs.core.Response.Status.Family.SERVER_ERROR;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/")
@ApplicationScoped
public class GameResource {

    @Inject
    @RestClient
    WalletClient walletClient;

    @Inject
    @RestClient
    HistoryClient historyClient;

    @POST
    @LRA(value = LRA.Type.REQUIRES_NEW, cancelOnFamily = {CLIENT_ERROR, SERVER_ERROR}, timeLimit = 5)
    @Path("/play")
    public void playGame(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, PlayGameRequest playGameRequest) throws InterruptedException {
        String player = playGameRequest.player();
        String roundRef = UUID.randomUUID().toString();
        long amount = 100;

        Thread.sleep(2);

        Instant timestamp = Instant.now();
        historyClient.createHistory(new HistoryRequest(player, roundRef, amount));
        System.out.println("History request duration: " + Duration.between(timestamp, Instant.now()).toMillis() + "ms");

        Thread.sleep(2);

        walletClient.bookMoney(new MoneyRequest(player, amount, "EUR"));

        Thread.sleep(2);
        System.out.println("Successfully finished " + lraId);
    }

    @PUT
    @Complete
    @Path("completeGameplay")
    public void completeGameplay(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, String userData) throws InterruptedException {
        System.out.println("Completing Saga " + lraId);
        Thread.sleep(2);
    }

    @PUT
    @Compensate
    @Path("compensateGameplay")
    public void compensateGameplay(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, String userData) throws InterruptedException {
        System.out.println("Compensating Saga " + lraId);
        Thread.sleep(2);
    }

}
