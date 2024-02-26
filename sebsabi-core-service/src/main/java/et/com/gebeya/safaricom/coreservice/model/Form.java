package et.com.gebeya.safaricom.coreservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import et.com.gebeya.safaricom.coreservice.dto.FormDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private int usageLimit;
    private Status status;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_username")
    private Client client;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "form_id")

    private List<FormQuestion> questions;

    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserResponse> userResponses;
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL)
    private List<Proposal> proposals;
    @ManyToOne
    @JoinColumn(name = "gig_worker_id")
    private GigWorker assignedGigWorker;
    public Form(FormDto formDto) {

        this.setDescription(formDto.getDescription());
        this.setTitle(formDto.getTitle());
        this.setUsageLimit(formDto.getUsageLimit());
        this.setStatus(formDto.getStatus());

    }
}
