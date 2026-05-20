package julien.roombookingsystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentName;

    @Column(nullable = false)
    private String studentId;

    @Column(nullable = false)
    private Long roomId;

    private LocalDate bookingDate;

    private String timeSlot;

    @Column(name = "released")
    private boolean release = true;
}
