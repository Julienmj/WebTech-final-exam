import axios from 'axios'

const api = axios.create({ baseURL: 'http://localhost:8080/api' })

// Rooms
export const fetchRooms = () => api.get('/rooms').then(r => r.data)
export const createRoom = (room) => api.post('/rooms', room).then(r => r.data)

// Bookings
export const fetchBookings = () => api.get('/bookings').then(r => r.data)
export const createBooking = (booking) => api.post('/bookings', booking).then(r => r.data)
export const updateBooking = (id, booking) => api.put(`/bookings/${id}`, booking).then(r => r.data)
export const deleteBooking = (id) => api.delete(`/bookings/${id}`)
