package et.com.gebeya.safaricom.coreservice.controller;
import et.com.gebeya.safaricom.coreservice.dto.ProposalDto;
import et.com.gebeya.safaricom.coreservice.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/core/proposals")
public class ProposalController {
    private final ProposalService proposalService;

    @Autowired
    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitProposal(@RequestBody ProposalDto proposalDto) {
        proposalService.submitProposal(proposalDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Proposal submitted successfully");
    }

}
