package et.com.gebeya.safaricom.coreservice.controller;

import et.com.gebeya.safaricom.coreservice.dto.ClientRequest;
import et.com.gebeya.safaricom.coreservice.dto.ClientResponse;
import et.com.gebeya.safaricom.coreservice.dto.ProposalDto;
import et.com.gebeya.safaricom.coreservice.model.Form;
import et.com.gebeya.safaricom.coreservice.service.ClientService;
import et.com.gebeya.safaricom.coreservice.service.FormService;
import et.com.gebeya.safaricom.coreservice.service.ProposalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/core/client")
public class ClientController {
    private final ClientService clientService;
    private final ProposalService proposalService;
    private final FormService formService;
   @PostMapping("/signup")
   @ResponseStatus(HttpStatus.CREATED)
//   @CircuitBreaker(name = "identity",fallbackMethod = "fallBackMethod")
//   @TimeLimiter(name = "identity")
//   @Retry(name = "identity")
//   @RolesAllowed("CLIENTS")
    public CompletableFuture<String> createClients(@RequestBody ClientRequest clientRequest)
   {
       return  CompletableFuture.supplyAsync(()->clientService.createClients(clientRequest));
   }

   public CompletableFuture<String> fallBackMethod(ClientRequest clientRequest,RuntimeException runtimeException){
       return CompletableFuture.supplyAsync(()->"Oops! Something went wrong , please Try signing up after some time.");
   }


    @GetMapping("/view")
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClientById(@RequestParam Long userId){
        return clientService.getClientById(userId);
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public String getMessage(){
       return "see us";
    }
    @GetMapping("/view/proposal/{formId}")
    public ProposalDto getProposalByFormId(@PathVariable Long formId) {
        return proposalService.getProposalByFormId(formId);
    }
    @GetMapping("/view/form")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Form> getFormByClientId(@RequestParam Long client_id) {
        return formService.getFormByClientId(client_id);
    }

}
