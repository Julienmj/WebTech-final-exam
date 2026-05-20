package julien.roombookingsystem.service;

import julien.roombookingsystem.model.Room;
import julien.roombookingsystem.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository repo;

    @Override
    public List<Room> getAll() {
        return repo.findAll();
    }

    @Override
    public Room getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    @Override
    public Room create(Room room) {
        return repo.save(room);
    }

    @Override
    public Room update(Long id, Room room) {
        Room existing = getById(id);
        existing.setName(room.getName());
        existing.setCapacity(room.getCapacity());
        existing.setLocation(room.getLocation());
        existing.setAvailability(room.isAvailability());
        return repo.save(existing);
    }
}
