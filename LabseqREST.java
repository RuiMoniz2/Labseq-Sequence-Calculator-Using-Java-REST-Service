package org.acme;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * REST service class.
 */
@Path("/labseq")
public class LabseqREST {

    /**
     * Get Labseq Value endpoint.
     *
     * @param n Index of the sequence.
     * @return JSON response.
     */
    @GET
    @Path("/{n}")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getLabseq(@PathParam("n") BigInteger n) {
        if (n.compareTo(BigInteger.ZERO) < 0) {                 //IF N<0 RETURNS ERROR MESSAGE
            JsonObject resp = Json.createObjectBuilder()
                    .add("parameter", n)
                    .add("labseq", -1)
                    .add("time", -1)
                    .add("status", "Error: Parameter out of bounds (n >= 0)")
                    .build();
            return resp;
        } else {                                                //IF N>=0 CALCULATE THE VALUE OF THE SEQUENCE FOR THE GIVE N AND RETURNS IT
            long startTime = System.currentTimeMillis();
            BigInteger result = calculateLabseq(n);
            long finishTime = System.currentTimeMillis();
            long calculationTime = (finishTime - startTime);
            JsonObject resp = Json.createObjectBuilder()
                    .add("parameter", n)
                    .add("labseq", result)
                    .add("time", calculationTime)
                    .add("status", "Success")
                    .build();
            return resp;
        }
    }

    /**
     * Calculate the value of the sequence for the given index.
     *
     * @param n Index of the sequence.
     * @return Calculated sequence's value.
     */
    private BigInteger calculateLabseq(BigInteger n) {
        if (n.equals(BigInteger.ZERO) || n.equals(BigInteger.valueOf(2))) {         // IF N == 0 || N == 2 RETURN 0
            return BigInteger.ZERO;
        }
        if (n.equals(BigInteger.ONE) || n.equals(BigInteger.valueOf(3))) {          // IF N == 1 || N == 3 RETURN 1       
            return BigInteger.ONE;
        }

        //INITIALIZE CACHE WITH THE FIRST VALUES OF THE SEQUENCE
        ArrayList<BigInteger> cache = new ArrayList<>();
        cache.add(BigInteger.ZERO);                                            
        cache.add(BigInteger.ONE);
        cache.add(BigInteger.ZERO);
        cache.add(BigInteger.ONE);

        BigInteger sum = BigInteger.ZERO;    //AUXILIAR VARIABLE TO STORE THE VALUE OF THE SEQUENCE
        //LOOP THAT CALCULATES THE NEXT ELEMENT OF THE SEQUENCE STORE IT IN THE CACHE AND REMOVE THE FIRST ONE WHO WILL NOT BE NEEDED ANYMORE
        for (int i = 4; n.compareTo(BigInteger.valueOf(i)) >= 0; i++) {
            sum = cache.get(0).add(cache.get(1)); 
            cache.add(sum);
            cache.remove(0);
        }
        return sum;
    }
}
