package movieBookingapplication.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name="theaters")
@JsonPropertyOrder({"id", "theatername", "address", "location"})
public class Theater {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String theatername;
    private String address;
    private String location;

    @OneToMany(mappedBy = "theater", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Screen> screens;

}
