package julien.roombookingsystem.repository;

import julien.roombookingsystem.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByRoomIdAndBookingDate(Long roomId, LocalDate bookingDate);
    List<Booking> findByRoomId(Long roomId);
    boolean existsByRoomIdAndBookingDateAndTimeSlot(Long roomId, LocalDate bookingDate, String timeSlot);
}
