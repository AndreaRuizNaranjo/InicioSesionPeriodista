package restServices;

import configuration.jwtConfiguration.JsonTokenNeeded;
import java.net.UnknownHostException;
import models.Device;
import response.AuthorizationResponse;
import response.BaseResponse;
import response.DeviceCollectionResponse;
import util.JwTokenHelper;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.GET;
import models.Periodista;
import models.PeriodistaDAO;

@Path("/")
public class HomeApiService extends BaseApiService {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @POST
    @Path("/authorization_service")
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizationService(@HeaderParam(USERNAME) String userName, @HeaderParam(PASSWORD) String password) throws UnknownHostException {
        System.out.println("sadfasdfasdfadfs");
        if (userName.isEmpty()) {
            return getResponse(new BaseResponse(USERNAME + " field cannot be empty", BaseResponse.FAILURE));
        } else if (password.isEmpty()) {
            return getResponse(new BaseResponse(PASSWORD + " field cannot be empty", BaseResponse.FAILURE));
        }

        boolean validado = false;
        Periodista periodistaRetorno = PeriodistaDAO.getPeriodista(userName);
        
        if(periodistaRetorno != null){
            if (password.equals(periodistaRetorno.getContrasena())) {
                validado=true;
            }
        }
        if (validado) {
            String privateKey = JwTokenHelper.getInstance().generatePrivateKey(userName, password);
            return getResponse(new AuthorizationResponse(BaseResponse.SUCCESS, "You're authenticated successfully. Private key will be valid for 30 mins", privateKey));
        } else {
            return getResponse(new BaseResponse(USERNAME + " Contraseña o usuario invalido", BaseResponse.FAILURE));
        }

    }

    @GET
    @Path("/allDevices")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public List<Periodista> getPeriodista_JSON() throws UnknownHostException {
        List<Periodista> listOfPeriodista = PeriodistaDAO.getAllPeriodista();
        return listOfPeriodista;
    }
}
