package dev.bbulk.mp_lra.wallet;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.lra.annotation.Compensate;
import org.eclipse.microprofile.lra.annotation.Complete;
import org.eclipse.microprofile.lra.annotation.ws.rs.LRA;

import java.net.URI;

import static jakarta.ws.rs.core.Response.Status.Family.CLIENT_ERROR;
import static jakarta.ws.rs.core.Response.Status.Family.SERVER_ERROR;
import static org.eclipse.microprofile.lra.annotation.ws.rs.LRA.LRA_HTTP_CONTEXT_HEADER;

@Path("/")
@ApplicationScoped
public class WalletResource {

    @POST
    @LRA(value = LRA.Type.MANDATORY, end = false, cancelOnFamily = {CLIENT_ERROR, SERVER_ERROR})
    @Path("booking")
    public void bookMoney(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, MoneyRequest moneyRequest) throws InterruptedException {
        Thread.sleep(10);
    }

    @PUT
    @Complete
    @Path("completeBooking")
    public void completeBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, String userData) throws InterruptedException {
        System.out.println("Completing Saga " + lraId);
        Thread.sleep(2);
    }

    @PUT
    @Compensate
    @Path("compensateBooking")
    public void compensateBooking(@HeaderParam(LRA_HTTP_CONTEXT_HEADER) URI lraId, String userData) throws InterruptedException {
        System.out.println("Compensating Saga " + lraId);
        Thread.sleep(2);
    }

}
