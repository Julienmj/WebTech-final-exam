package julien.roombookingsystem.service;

import julien.roombookingsystem.model.Booking;
import julien.roombookingsystem.model.Room;
import julien.roombookingsystem.repository.BookingRepository;
import julien.roombookingsystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private static final Set<String> ALL_SLOTS = Set.of(
            "08:00 - 10:00", "10:00 - 12:00", "14:00 - 16:00"
    );

    private final BookingRepository bookingRepo;
    private final RoomRepository roomRepo;

    @Override
    public List<Booking> getAll() {
        return bookingRepo.findAll();
    }

    @Override
    public Booking create(Booking booking) {
        roomRepo.findById(booking.getRoomId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room not found"));

        if (bookingRepo.existsByRoomIdAndBookingDateAndTimeSlot(
                booking.getRoomId(), booking.getBookingDate(), booking.getTimeSlot())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This time slot is already booked for that room on that date");
        }

        Booking saved = bookingRepo.save(booking);
        refreshRoomAvailability(booking.getRoomId(), booking.getBookingDate());
        return saved;
    }

    @Override
    public void delete(Long id) {
        Booking booking = bookingRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
        Long roomId = booking.getRoomId();
        bookingRepo.deleteById(id);
        refreshRoomAvailability(roomId, booking.getBookingDate());
    }

    @Override
    public Booking update(Long id, Booking incoming) {
        Booking existing = bookingRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));

        // If slot or date changed, check for conflict on the new slot
        boolean slotChanged = !existing.getTimeSlot().equals(incoming.getTimeSlot())
                || !existing.getBookingDate().equals(incoming.getBookingDate())
                || !existing.getRoomId().equals(incoming.getRoomId());

        if (slotChanged && bookingRepo.existsByRoomIdAndBookingDateAndTimeSlot(
                incoming.getRoomId(), incoming.getBookingDate(), incoming.getTimeSlot())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "This time slot is already booked for that room on that date");
        }

        Long oldRoomId = existing.getRoomId();

        existing.setStudentName(incoming.getStudentName());
        existing.setStudentId(incoming.getStudentId());
        existing.setRoomId(incoming.getRoomId());
        existing.setBookingDate(incoming.getBookingDate());
        existing.setTimeSlot(incoming.getTimeSlot());

        Booking saved = bookingRepo.save(existing);

        // Refresh availability for old room (if room changed) and new room
        refreshRoomAvailability(oldRoomId, incoming.getBookingDate());
        if (!oldRoomId.equals(incoming.getRoomId())) {
            refreshRoomAvailability(incoming.getRoomId(), incoming.getBookingDate());
        }

        return saved;
    }

    // A room is unavailable only when all 3 slots are booked on a given date
    private void refreshRoomAvailability(Long roomId, java.time.LocalDate date) {
        Room room = roomRepo.findById(roomId).orElse(null);
        if (room == null) return;

        List<Booking> bookingsOnDate = bookingRepo.findByRoomIdAndBookingDate(roomId, date);
        Set<String> bookedSlots = new java.util.HashSet<>();
        for (Booking b : bookingsOnDate) bookedSlots.add(b.getTimeSlot());

        room.setAvailability(!bookedSlots.containsAll(ALL_SLOTS));
        roomRepo.save(room);
    }
}
