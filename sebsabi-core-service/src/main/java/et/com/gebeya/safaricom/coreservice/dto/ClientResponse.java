package et.com.gebeya.safaricom.coreservice.dto;

import et.com.gebeya.safaricom.coreservice.model.Client;
import et.com.gebeya.safaricom.coreservice.model.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String companyName;
    private String companyType;
    private String occupation;
    private Status isActive;

    public ClientResponse(Client client){
        this.id=client.getId();
        this.firstName = client.getFirstName();
        this.lastName= client.getLastName();
        this.companyName= client.getCompanyName();
        this.email=client.getEmail();
        this.companyType=client.getCompanyType();
        this.occupation=client.getOccupation();
    }
}
