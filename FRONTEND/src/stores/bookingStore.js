import { defineStore } from 'pinia'
import { ref } from 'vue'
import { fetchBookings, createBooking, updateBooking, deleteBooking } from '../services/api.js'

export const useBookingStore = defineStore('bookings', () => {
  const bookings = ref([])

  async function load() {
    bookings.value = await fetchBookings()
  }

  async function add(booking) {
    const created = await createBooking(booking)
    bookings.value.push(created)
  }

  async function update(id, booking) {
    const updated = await updateBooking(id, booking)
    const i = bookings.value.findIndex(b => b.id === id)
    if (i !== -1) bookings.value[i] = updated
  }

  async function remove(id) {
    await deleteBooking(id)
    bookings.value = bookings.value.filter(b => b.id !== id)
  }

  return { bookings, load, add, update, remove }
})
