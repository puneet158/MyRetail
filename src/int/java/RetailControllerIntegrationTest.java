import com.mongodb.MongoClient;
import com.retail.myretail.MyretailApplication;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.process.runtime.Network;
import junit.framework.TestCase;
import de.flapdoodle.embed.mongo.distribution.Version;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyretailApplication.class)
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RetailControllerIntegrationTest extends TestCase {

    private static final MongodStarter starter = MongodStarter.getDefaultInstance();

    @Autowired
    private MockMvc mockMvc;

    private MongodExecutable mongodExe;
    private MongodProcess mongod;
    private MongoClient mongo;


    @Override
    protected void setUp() throws Exception {
        mongodExe = starter.prepare(new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)
                .net(new Net("localhost", 12345, Network.localhostIsIPv6()))
                .build());
        mongod = mongodExe.start();
        super.setUp();
        mongo = new MongoClient("localhost", 12345);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mongod.stop();
        mongodExe.stop();
    }

    @Test
    public void shouldGetProductInfo() throws Exception {
        mockMvc.perform(get("/products/13860428")).andExpect(status().isOk()).andExpect(content().json("{\n" +
                "\"id\": 13860428,\n" +
                "\"name\": \"The Big Lebowski (Blu-ray)\",\n" +
                "\"current_price\": {\n" +
                "\"value\": 25.49,\n" +
                "\"currency_code\": \"USD\"\n" +
                "}\n" +
                "}"));
    }


    @Test
    public void shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/products/1")).andExpect(status().isNotFound());
    }

    @Test
    public void shouldUpdatePoductPrice() throws Exception {
        String priceUpdateRequest = "{\n" +
                "   \"value\": 80,\n" +
                "    \"currency_code\": \"USD\"\n" +
                "}";

        this.mockMvc
                .perform(put("/products/13860428").contentType(MediaType.APPLICATION_JSON).content(priceUpdateRequest))
                .andExpect(status().isOk()).andExpect(content().json("{\n" +
                "\"id\": 13860428,\n" +
                "\"name\": \"The Big Lebowski (Blu-ray)\",\n" +
                "\"current_price\": {\n" +
                "\"value\": 80.00,\n" +
                "\"currency_code\": \"USD\"\n" +
                "}\n" +
                "}"));

    }

    @Test
    public void shouldReturnErrorOnInvalidPriceUpdateRequest() throws Exception {
        String priceUpdateRequest = "{\n" +
                "    \"currency_code\": \"USD\"\n" +
                "}";
        this.mockMvc
                .perform(put("/products/13860428").contentType(MediaType.APPLICATION_JSON).content(priceUpdateRequest))
                .andExpect(status().isBadRequest());
    }

}
