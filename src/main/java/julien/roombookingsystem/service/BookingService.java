package julien.roombookingsystem.service;

import julien.roombookingsystem.model.Booking;
import java.util.List;

public interface BookingService {
    List<Booking> getAll();
    Booking create(Booking booking);
    void delete(Long id);
    Booking update(Long id, Booking booking);
}
