package julien.roombookingsystem.service;

import julien.roombookingsystem.model.Room;
import java.util.List;

public interface RoomService {
    List<Room> getAll();
    Room getById(Long id);
    Room create(Room room);
    Room update(Long id, Room room);
}
